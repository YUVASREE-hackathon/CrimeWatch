package com.crimewatch.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String fullName;
    @Column(nullable = false, unique = true, length = 160)
    private String email;
    @JsonIgnore @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Role role;
    @ManyToOne(optional = false) @JoinColumn(name = "role_id")
    private RoleDefinition roleDefinition;
    @Column(length = 20)
    private String phone;
    @Column(length = 300)
    private String address;
    @Builder.Default
    private boolean enabled = true;
    @Builder.Default @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}

