package com.crimewatch.service;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import com.crimewatch.web.dto.ReportDtos.EvidenceView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service @RequiredArgsConstructor
public class EvidenceService {
    private final ReportRepository reports;
    private final EvidenceRepository evidence;
    private final CurrentUserService currentUser;
    private final AuditService audit;
    private final Path uploadRoot = Paths.get("uploads").toAbsolutePath().normalize();

    @Transactional
    public EvidenceView upload(String publicId, MultipartFile file) {
        if (file.isEmpty() || file.getSize() > 10 * 1024 * 1024) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Evidence file must be between 1 byte and 10 MB");
        AppUser actor = currentUser.get();
        CrimeReport report = reports.findByPublicId(publicId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report not found"));
        boolean owner = report.getReporter().getId().equals(actor.getId());
        boolean assigned = report.getAssignedOfficer() != null && report.getAssignedOfficer().getId().equals(actor.getId());
        if (!owner && !assigned && actor.getRole() != Role.ADMIN) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot add evidence to this report");
        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "evidence" : file.getOriginalFilename());
        if (original.contains("..")) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file name");
        try {
            Files.createDirectories(uploadRoot);
            String stored = UUID.randomUUID() + "-" + original.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path target = uploadRoot.resolve(stored).normalize();
            if (!target.startsWith(uploadRoot)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid storage path");
            file.transferTo(target);
            var saved = evidence.save(Evidence.builder().report(report).fileName(original).storagePath(target.toString())
                .contentType(file.getContentType()).fileSize(file.getSize()).uploadedBy(actor).build());
            audit.record(actor, "EVIDENCE_ADDED", "CRIME_REPORT", publicId, original);
            return new EvidenceView(saved.getId(), saved.getFileName(), saved.getContentType(), saved.getFileSize(), saved.getUploadedAt());
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to store evidence file");
        }
    }
}
