package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "actor_id")
    private AppUser actor;
    @Column(nullable = false, length = 80)
    private String action;
    @Column(nullable = false, length = 80)
    private String entityType;
    @Column(length = 80)
    private String entityId;
    @Column(columnDefinition = "TEXT")
    private String details;
    @Column(length = 64)
    private String ipAddress;
    @Builder.Default @Column(nullable = false)
    private Instant createdAt = Instant.now();
}
