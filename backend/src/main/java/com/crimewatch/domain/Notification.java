package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "user_id")
    private AppUser user;
    @ManyToOne @JoinColumn(name = "report_id")
    private CrimeReport report;
    @Column(nullable = false, length = 500)
    private String message;
    @Builder.Default
    private boolean readFlag = false;
    @Builder.Default @Column(nullable = false)
    private Instant createdAt = Instant.now();
}

