package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Entity
@Table(name = "crime_reports", indexes = {
    @Index(name = "idx_report_public_id", columnList = "publicId"),
    @Index(name = "idx_report_status", columnList = "status"),
    @Index(name = "idx_report_city", columnList = "city")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrimeReport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 30)
    private String publicId;
    @ManyToOne(optional = false) @JoinColumn(name = "category_id")
    private CrimeCategory category;
    @Column(nullable = false, length = 180)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private LocalDate incidentDate;
    @Column(nullable = false)
    private LocalTime incidentTime;
    @Column(nullable = false, length = 240)
    private String location;
    @Column(nullable = false, length = 100)
    private String city;
    @Column(length = 120)
    private String area;
    private Double latitude;
    private Double longitude;
    @Column(columnDefinition = "TEXT")
    private String suspectInformation;
    @Column(columnDefinition = "TEXT")
    private String witnessInformation;
    @Column(columnDefinition = "TEXT")
    private String evidenceDescription;
    @Column(columnDefinition = "TEXT")
    private String additionalRemarks;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    @Builder.Default
    private ReportStatus status = ReportStatus.SUBMITTED;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    @Builder.Default
    private Priority priority = Priority.MEDIUM;
    @ManyToOne(optional = false) @JoinColumn(name = "reporter_id")
    private AppUser reporter;
    @ManyToOne @JoinColumn(name = "assigned_officer_id")
    private AppUser assignedOfficer;
    @Builder.Default @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
    @Builder.Default @Column(nullable = false)
    private Instant updatedAt = Instant.now();
    @PreUpdate void touch() { updatedAt = Instant.now(); }
}

