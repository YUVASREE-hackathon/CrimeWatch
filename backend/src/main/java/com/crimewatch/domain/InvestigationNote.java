package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "investigation_notes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InvestigationNote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "report_id")
    private CrimeReport report;
    @ManyToOne(optional = false) @JoinColumn(name = "author_id")
    private AppUser author;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String note;
    @Builder.Default
    private boolean internalOnly = true;
    @Builder.Default @Column(nullable = false)
    private Instant createdAt = Instant.now();
}

