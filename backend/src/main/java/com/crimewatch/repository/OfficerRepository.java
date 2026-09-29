package com.crimewatch.repository;

import com.crimewatch.domain.OfficerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OfficerRepository extends JpaRepository<OfficerProfile, Long> {
    Optional<OfficerProfile> findByUserId(Long userId);
}

