package com.crimewatch.repository;

import com.crimewatch.domain.ReportStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatusHistoryRepository extends JpaRepository<ReportStatusHistory, Long> {
    List<ReportStatusHistory> findByReportIdOrderByChangedAtAsc(Long reportId);
}

