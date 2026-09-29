package com.crimewatch.web.dto;

import com.crimewatch.domain.*;
import jakarta.validation.constraints.*;
import java.time.*;
import java.util.List;

public final class ReportDtos {
    private ReportDtos() {}

    public record ReportRequest(
        @NotNull Long categoryId,
        @NotBlank @Size(min = 5, max = 180) String title,
        @NotBlank @Size(min = 20, max = 5000) String description,
        @NotNull @PastOrPresent LocalDate incidentDate,
        @NotNull LocalTime incidentTime,
        @NotBlank @Size(max = 240) String location,
        @NotBlank @Size(max = 100) String city,
        @Size(max = 120) String area,
        @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        @Size(max = 3000) String suspectInformation,
        @Size(max = 3000) String witnessInformation,
        @Size(max = 3000) String evidenceDescription,
        @Size(max = 3000) String additionalRemarks
    ) {}

    public record StatusRequest(@NotNull ReportStatus status, @Size(max = 500) String comment) {}
    public record AssignRequest(@NotNull Long officerId, Priority priority) {}
    public record NoteRequest(@NotBlank @Size(min = 3, max = 5000) String note, Boolean internalOnly) {}

    public record UserBrief(Long id, String fullName, String email) {}
    public record CategoryView(Long id, String name, String description) {}
    public record HistoryView(Long id, ReportStatus fromStatus, ReportStatus toStatus, String comment, String changedBy, Instant changedAt) {}
    public record NoteView(Long id, String note, boolean internalOnly, String author, Instant createdAt) {}
    public record EvidenceView(Long id, String fileName, String contentType, Long fileSize, Instant uploadedAt) {}

    public record ReportView(
        Long id, String publicId, CategoryView category, String title, String description,
        LocalDate incidentDate, LocalTime incidentTime, String location, String city, String area,
        Double latitude, Double longitude, String suspectInformation, String witnessInformation,
        String evidenceDescription, String additionalRemarks, ReportStatus status, Priority priority,
        UserBrief reporter, UserBrief assignedOfficer, Instant createdAt, Instant updatedAt,
        List<HistoryView> history, List<NoteView> notes, List<EvidenceView> evidence
    ) {}
}

