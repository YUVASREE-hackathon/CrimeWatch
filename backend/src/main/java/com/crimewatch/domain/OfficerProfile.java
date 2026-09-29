package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "officers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OfficerProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false) @JoinColumn(name = "user_id", unique = true)
    private AppUser user;
    @Column(nullable = false, unique = true, length = 40)
    private String badgeNumber;
    @Column(length = 80)
    private String department;
    @Column(length = 80)
    private String rankName;
    @Builder.Default
    private boolean available = true;
}

