package com.crimewatch.service;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import com.crimewatch.web.dto.ReportDtos.*;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;

@Service @RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reports;
    private final CategoryRepository categories;
    private final UserRepository users;
    private final StatusHistoryRepository histories;
    private final NoteRepository notes;
    private final EvidenceRepository evidence;
    private final NotificationRepository notifications;
    private final CurrentUserService currentUser;
    private final AuditService audit;

    @Transactional
    public ReportView create(ReportRequest request) {
        AppUser actor = currentUser.get();
        var category = categories.findById(request.categoryId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown crime category"));
        String publicId = "CR-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        var report = reports.save(CrimeReport.builder().publicId(publicId).category(category).title(request.title().trim())
            .description(request.description().trim()).incidentDate(request.incidentDate()).incidentTime(request.incidentTime())
            .location(request.location().trim()).city(request.city().trim()).area(request.area()).latitude(request.latitude())
            .longitude(request.longitude()).suspectInformation(request.suspectInformation()).witnessInformation(request.witnessInformation())
            .evidenceDescription(request.evidenceDescription()).additionalRemarks(request.additionalRemarks()).reporter(actor).build());
        histories.save(ReportStatusHistory.builder().report(report).toStatus(ReportStatus.SUBMITTED)
            .comment("Report submitted by citizen").changedBy(actor).build());
        notifications.save(Notification.builder().user(actor).report(report).message("Your report " + publicId + " has been submitted successfully.").build());
        audit.record(actor, "REPORT_CREATED", "CRIME_REPORT", publicId, report.getTitle());
        return view(report, true);
    }

    @Transactional(readOnly = true)
    public Page<ReportView> list(String search, ReportStatus status, Priority priority, Long categoryId,
                                 String city, LocalDate from, LocalDate to, int page, int size, String sort) {
        AppUser actor = currentUser.get();
        Specification<CrimeReport> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (actor.getRole() == Role.CITIZEN) predicates.add(cb.equal(root.get("reporter").get("id"), actor.getId()));
            if (actor.getRole() == Role.OFFICER) predicates.add(cb.equal(root.get("assignedOfficer").get("id"), actor.getId()));
            if (StringUtils.hasText(search)) {
                String pattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(cb.like(cb.lower(root.get("publicId")), pattern), cb.like(cb.lower(root.get("title")), pattern),
                    cb.like(cb.lower(root.get("location")), pattern), cb.like(cb.lower(root.get("city")), pattern)));
            }
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (priority != null) predicates.add(cb.equal(root.get("priority"), priority));
            if (categoryId != null) predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            if (StringUtils.hasText(city)) predicates.add(cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get("incidentDate"), from));
            if (to != null) predicates.add(cb.lessThanOrEqualTo(root.get("incidentDate"), to));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        String[] sortParts = Optional.ofNullable(sort).orElse("updatedAt,desc").split(",");
        Sort order = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") ? Sort.by(sortParts[0]).ascending() : Sort.by(sortParts[0]).descending();
        return reports.findAll(spec, PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 100), order)).map(r -> view(r, false));
    }

    @Transactional(readOnly = true)
    public ReportView get(String publicId) {
        CrimeReport report = require(publicId);
        assertCanView(report, currentUser.get());
        return view(report, true);
    }

    @Transactional
    public ReportView updateStatus(String publicId, StatusRequest request) {
        AppUser actor = currentUser.get();
        if (actor.getRole() == Role.CITIZEN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Citizens cannot change report status");
        CrimeReport report = require(publicId);
        if (actor.getRole() == Role.OFFICER && (report.getAssignedOfficer() == null || !report.getAssignedOfficer().getId().equals(actor.getId())))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This case is not assigned to you");
        validateTransition(report.getStatus(), request.status(), actor.getRole());
        ReportStatus old = report.getStatus();
        report.setStatus(request.status());
        reports.save(report);
        histories.save(ReportStatusHistory.builder().report(report).fromStatus(old).toStatus(request.status())
            .comment(request.comment()).changedBy(actor).build());
        notifications.save(Notification.builder().user(report.getReporter()).report(report)
            .message("Your report " + publicId + " status changed to " + request.status().name().replace('_', ' ') + ".").build());
        audit.record(actor, "STATUS_CHANGED", "CRIME_REPORT", publicId, old + " -> " + request.status());
        return view(report, true);
    }

    @Transactional
    public ReportView assign(String publicId, AssignRequest request) {
        AppUser actor = currentUser.get();
        if (actor.getRole() != Role.ADMIN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrators can assign cases");
        CrimeReport report = require(publicId);
        AppUser officer = users.findById(request.officerId()).filter(u -> u.getRole() == Role.OFFICER)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected user is not an officer"));
        ReportStatus old = report.getStatus();
        report.setAssignedOfficer(officer);
        report.setPriority(request.priority() == null ? report.getPriority() : request.priority());
        report.setStatus(ReportStatus.ASSIGNED);
        reports.save(report);
        histories.save(ReportStatusHistory.builder().report(report).fromStatus(old).toStatus(ReportStatus.ASSIGNED)
            .comment("Assigned to " + officer.getFullName()).changedBy(actor).build());
        notifications.save(Notification.builder().user(report.getReporter()).report(report)
            .message("Your report " + publicId + " has been assigned to an officer.").build());
        notifications.save(Notification.builder().user(officer).report(report).message("Case " + publicId + " has been assigned to you.").build());
        audit.record(actor, "REPORT_ASSIGNED", "CRIME_REPORT", publicId, "Officer: " + officer.getEmail());
        return view(report, true);
    }

    @Transactional
    public NoteView addNote(String publicId, NoteRequest request) {
        AppUser actor = currentUser.get();
        if (actor.getRole() == Role.CITIZEN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Citizens cannot add investigation notes");
        CrimeReport report = require(publicId);
        if (actor.getRole() == Role.OFFICER && (report.getAssignedOfficer() == null || !report.getAssignedOfficer().getId().equals(actor.getId())))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This case is not assigned to you");
        var note = notes.save(InvestigationNote.builder().report(report).author(actor).note(request.note().trim())
            .internalOnly(request.internalOnly() == null || request.internalOnly()).build());
        audit.record(actor, "NOTE_ADDED", "CRIME_REPORT", publicId, "Investigation note added");
        return new NoteView(note.getId(), note.getNote(), note.isInternalOnly(), actor.getFullName(), note.getCreatedAt());
    }

    @Transactional
    public void delete(String publicId) {
        AppUser actor = currentUser.get();
        if (actor.getRole() != Role.ADMIN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only administrators can delete reports");
        CrimeReport report = require(publicId);
        audit.record(actor, "REPORT_DELETED", "CRIME_REPORT", publicId, report.getTitle());
        reports.delete(report);
    }

    private CrimeReport require(String id) { return reports.findByPublicId(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found")); }

    private void assertCanView(CrimeReport report, AppUser actor) {
        if (actor.getRole() == Role.CITIZEN && !report.getReporter().getId().equals(actor.getId())) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Report does not belong to you");
        if (actor.getRole() == Role.OFFICER && (report.getAssignedOfficer() == null || !report.getAssignedOfficer().getId().equals(actor.getId()))) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Case is not assigned to you");
    }

    private void validateTransition(ReportStatus from, ReportStatus to, Role role) {
        if (from == to) return;
        Map<ReportStatus, Set<ReportStatus>> allowed = Map.of(
            ReportStatus.SUBMITTED, Set.of(ReportStatus.UNDER_REVIEW),
            ReportStatus.UNDER_REVIEW, Set.of(ReportStatus.ASSIGNED),
            ReportStatus.ASSIGNED, Set.of(ReportStatus.UNDER_INVESTIGATION),
            ReportStatus.UNDER_INVESTIGATION, Set.of(ReportStatus.RESOLVED),
            ReportStatus.RESOLVED, Set.of(ReportStatus.CLOSED),
            ReportStatus.CLOSED, Set.of());
        if (role != Role.ADMIN && !allowed.getOrDefault(from, Set.of()).contains(to))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid status transition from " + from + " to " + to);
    }

    public ReportView view(CrimeReport r, boolean details) {
        List<HistoryView> history = details ? histories.findByReportIdOrderByChangedAtAsc(r.getId()).stream()
            .map(h -> new HistoryView(h.getId(), h.getFromStatus(), h.getToStatus(), h.getComment(), h.getChangedBy().getFullName(), h.getChangedAt())).toList() : List.of();
        List<NoteView> noteViews = details ? notes.findByReportIdOrderByCreatedAtDesc(r.getId()).stream()
            .map(n -> new NoteView(n.getId(), n.getNote(), n.isInternalOnly(), n.getAuthor().getFullName(), n.getCreatedAt())).toList() : List.of();
        List<EvidenceView> evidenceViews = details ? evidence.findByReportIdOrderByUploadedAtDesc(r.getId()).stream()
            .map(e -> new EvidenceView(e.getId(), e.getFileName(), e.getContentType(), e.getFileSize(), e.getUploadedAt())).toList() : List.of();
        return new ReportView(r.getId(), r.getPublicId(), new CategoryView(r.getCategory().getId(), r.getCategory().getName(), r.getCategory().getDescription()),
            r.getTitle(), r.getDescription(), r.getIncidentDate(), r.getIncidentTime(), r.getLocation(), r.getCity(), r.getArea(), r.getLatitude(), r.getLongitude(),
            r.getSuspectInformation(), r.getWitnessInformation(), r.getEvidenceDescription(), r.getAdditionalRemarks(), r.getStatus(), r.getPriority(),
            brief(r.getReporter()), brief(r.getAssignedOfficer()), r.getCreatedAt(), r.getUpdatedAt(), history, noteViews, evidenceViews);
    }
    private UserBrief brief(AppUser u) { return u == null ? null : new UserBrief(u.getId(), u.getFullName(), u.getEmail()); }
}
