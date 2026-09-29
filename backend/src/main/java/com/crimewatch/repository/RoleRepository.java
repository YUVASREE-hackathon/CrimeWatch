package com.crimewatch.repository;

import com.crimewatch.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleDefinition, Long> {
    Optional<RoleDefinition> findByCode(Role code);
}

