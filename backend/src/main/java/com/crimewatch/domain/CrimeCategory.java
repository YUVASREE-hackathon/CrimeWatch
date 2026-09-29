package com.crimewatch.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crime_categories")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CrimeCategory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 80)
    private String name;
    @Column(length = 300)
    private String description;
    @Builder.Default
    private boolean active = true;
}

