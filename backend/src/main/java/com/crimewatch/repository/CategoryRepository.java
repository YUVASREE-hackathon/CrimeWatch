package com.crimewatch.repository;

import com.crimewatch.domain.CrimeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CrimeCategory, Long> {
    Optional<CrimeCategory> findByNameIgnoreCase(String name);
}

