package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "evidence")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Evidence {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "report_id")
    private CrimeReport report;
    @Column(nullable = false, length = 200)
    private String fileName;
    @Column(nullable = false, length = 500)
    private String storagePath;
    @Column(length = 100)
    private String contentType;
    private Long fileSize;
    @ManyToOne(optional = false) @JoinColumn(name = "uploaded_by")
    private AppUser uploadedBy;
    @Builder.Default
    private Instant uploadedAt = Instant.now();
}

