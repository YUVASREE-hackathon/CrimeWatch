package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "report_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReportStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "report_id")
    private CrimeReport report;
    @Enumerated(EnumType.STRING) @Column(length = 30)
    private ReportStatus fromStatus;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 30)
    private ReportStatus toStatus;
    @Column(length = 500)
    private String comment;
    @ManyToOne(optional = false) @JoinColumn(name = "changed_by")
    private AppUser changedBy;
    @Builder.Default @Column(nullable = false)
    private Instant changedAt = Instant.now();
}

