package com.crimewatch.config;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Component @RequiredArgsConstructor
@ConditionalOnProperty(name = "app.demo-data", havingValue = "true", matchIfMissing = true)
public class DemoDataSeeder implements CommandLineRunner {
    private final UserRepository users;
    private final RoleRepository roles;
    private final OfficerRepository officers;
    private final CategoryRepository categories;
    private final ReportRepository reports;
    private final StatusHistoryRepository histories;
    private final NoteRepository notes;
    private final NotificationRepository notifications;
    private final AuditLogRepository audits;
    private final PasswordEncoder encoder;

    @Override @Transactional
    public void run(String... args) {
        if (users.count() > 0) return;
        RoleDefinition citizenRole = roles.save(RoleDefinition.builder().code(Role.CITIZEN).displayName("Citizen").build());
        RoleDefinition officerRole = roles.save(RoleDefinition.builder().code(Role.OFFICER).displayName("Police Officer").build());
        RoleDefinition adminRole = roles.save(RoleDefinition.builder().code(Role.ADMIN).displayName("Administrator").build());
        var categoryNames = List.of("Theft", "Burglary", "Assault", "Fraud", "Cybercrime", "Missing Person", "Vandalism", "Harassment", "Accident", "Other");
        List<CrimeCategory> categoryList = new ArrayList<>();
        for (String name : categoryNames) categoryList.add(categories.save(CrimeCategory.builder().name(name).description("Reports involving " + name.toLowerCase() + " incidents").build()));

        AppUser admin = users.save(AppUser.builder().fullName("Aarav Sharma").email("admin@crimewatch.demo")
            .password(encoder.encode("Admin@123")).role(Role.ADMIN).roleDefinition(adminRole).phone("+91 90000 00001").address("Central Operations Centre").build());

        String[] officerNames = {"Meera Nair", "Vikram Singh", "Ananya Rao", "Kabir Patel", "Ishita Sen"};
        List<AppUser> officerUsers = new ArrayList<>();
        for (int i = 0; i < officerNames.length; i++) {
            AppUser officer = users.save(AppUser.builder().fullName(officerNames[i]).email("officer" + (i + 1) + "@crimewatch.demo")
                .password(encoder.encode("Officer@123")).role(Role.OFFICER).roleDefinition(officerRole).phone("+91 90000 0010" + i).address("CrimeWatch District Unit").build());
            officerUsers.add(officer);
            officers.save(OfficerProfile.builder().user(officer).badgeNumber("CW-20" + (31 + i)).department(i % 2 == 0 ? "Field Investigation" : "Cyber & Fraud")
                .rankName(i == 0 ? "Inspector" : "Sub-Inspector").available(i != 4).build());
        }

        String[] citizenNames = {"Rohan Mehta", "Priya Iyer", "Aditya Kumar", "Neha Joshi", "Arjun Das", "Kavya Menon", "Rahul Gupta", "Sneha Roy", "Dev Malhotra", "Nisha Verma", "Sanjay Bose", "Pooja Reddy", "Varun Shah", "Diya Kapoor", "Manav Jain"};
        List<AppUser> citizens = new ArrayList<>();
        for (int i = 0; i < citizenNames.length; i++) citizens.add(users.save(AppUser.builder().fullName(citizenNames[i])
            .email(i == 0 ? "citizen@crimewatch.demo" : "citizen" + (i + 1) + "@crimewatch.demo")
            .password(encoder.encode("Citizen@123")).role(Role.CITIZEN).roleDefinition(citizenRole).phone("+91 91000 002" + String.format("%02d", i)).address("Demo residential address " + (i + 1)).build()));

        String[] cities = {"Chennai", "Bengaluru", "Hyderabad", "Pune", "Kochi", "Coimbatore"};
        String[] areas = {"Central Ward", "Lake View", "Market District", "North Avenue", "Tech Park", "Railway Colony"};
        String[] titles = {"Mobile phone stolen near bus stop", "Suspicious online payment request", "Damage to parked vehicle", "Residential break-in reported", "Harassment near public transit", "Missing documents and wallet", "Unauthorized account access", "Road collision requiring review", "Shop inventory theft", "Suspicious activity near residence"};
        ReportStatus[] statuses = ReportStatus.values();
        Priority[] priorities = Priority.values();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 30; i++) {
            AppUser citizen = citizens.get(i % citizens.size());
            ReportStatus status = statuses[i % statuses.length];
            AppUser assigned = status.ordinal() >= ReportStatus.ASSIGNED.ordinal() ? officerUsers.get(i % officerUsers.size()) : null;
            var report = reports.save(CrimeReport.builder().publicId(String.format("CR-%d-%04d", today.getYear(), 1001 + i))
                .category(categoryList.get(i % categoryList.size())).title(titles[i % titles.length])
                .description("Synthetic demonstration report describing the incident, surrounding circumstances, and information available to investigators. No real person or event is represented.")
                .incidentDate(today.minusDays((i * 11L) % 330)).incidentTime(LocalTime.of(7 + (i % 14), (i * 7) % 60))
                .location((10 + i) + " " + areas[i % areas.length] + " Road").city(cities[i % cities.length]).area(areas[i % areas.length])
                .latitude(12.80 + (i * .01)).longitude(80.10 + (i * .01)).suspectInformation(i % 3 == 0 ? "Description supplied; identity unknown." : null)
                .witnessInformation(i % 4 == 0 ? "One synthetic witness statement is available." : null).evidenceDescription(i % 2 == 0 ? "Reference photographs and written description recorded." : "Written description recorded.")
                .additionalRemarks("Created automatically as synthetic demonstration data.").status(status).priority(priorities[i % priorities.length])
                .reporter(citizen).assignedOfficer(assigned).createdAt(Instant.now().minusSeconds(i * 86400L)).updatedAt(Instant.now().minusSeconds(i * 3600L)).build());

            histories.save(ReportStatusHistory.builder().report(report).toStatus(ReportStatus.SUBMITTED).comment("Report received through citizen portal").changedBy(citizen).changedAt(report.getCreatedAt()).build());
            ReportStatus previous = ReportStatus.SUBMITTED;
            for (int s = 1; s <= status.ordinal(); s++) {
                ReportStatus next = statuses[s];
                histories.save(ReportStatusHistory.builder().report(report).fromStatus(previous).toStatus(next).comment(historyComment(next, assigned))
                    .changedBy(s == 1 ? admin : assigned == null ? admin : assigned).changedAt(report.getCreatedAt().plusSeconds(s * 7200L)).build());
                previous = next;
            }
            if (assigned != null) notes.save(InvestigationNote.builder().report(report).author(assigned)
                .note("Initial review completed. Evidence checklist and follow-up actions recorded for this synthetic case.").internalOnly(true).createdAt(report.getCreatedAt().plusSeconds(10800)).build());
            notifications.save(Notification.builder().user(citizen).report(report).message("Update available for report " + report.getPublicId() + ": " + status.name().replace('_', ' ')).readFlag(i % 3 == 0).createdAt(report.getUpdatedAt()).build());
        }
        audits.save(AuditLog.builder().actor(admin).action("DEMO_DATA_CREATED").entityType("SYSTEM").entityId("seed-v1").details("Created 21 users, 5 officer profiles, 10 categories and 30 synthetic reports").build());
    }

    private String historyComment(ReportStatus status, AppUser officer) {
        return switch (status) {
            case UNDER_REVIEW -> "Report reviewed for completeness";
            case ASSIGNED -> "Assigned to " + (officer == null ? "investigation team" : officer.getFullName());
            case UNDER_INVESTIGATION -> "Investigation activities started";
            case RESOLVED -> "Investigation outcome recorded";
            case CLOSED -> "Case formally closed";
            default -> "Status updated";
        };
    }
}
