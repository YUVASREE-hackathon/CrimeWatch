package com.crimewatch.repository;

import com.crimewatch.domain.AppUser;
import com.crimewatch.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    List<AppUser> findByRole(Role role);
    long countByRole(Role role);
}

