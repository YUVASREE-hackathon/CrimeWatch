package com.crimewatch.repository;

import com.crimewatch.domain.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findByReportIdOrderByUploadedAtDesc(Long reportId);
}

