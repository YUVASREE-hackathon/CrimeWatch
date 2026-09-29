package com.crimewatch.repository;

import com.crimewatch.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.*;
import java.util.*;

public interface ReportRepository extends JpaRepository<CrimeReport, Long>, JpaSpecificationExecutor<CrimeReport> {
    Optional<CrimeReport> findByPublicId(String publicId);
    List<CrimeReport> findTop6ByOrderByUpdatedAtDesc();
    List<CrimeReport> findByReporterIdOrderByUpdatedAtDesc(Long reporterId);
    List<CrimeReport> findByAssignedOfficerIdOrderByUpdatedAtDesc(Long officerId);
    long countByStatus(ReportStatus status);
    long countByReporterId(Long reporterId);
    long countByReporterIdAndStatus(Long reporterId, ReportStatus status);
    long countByAssignedOfficerId(Long officerId);
    long countByAssignedOfficerIdAndStatus(Long officerId, ReportStatus status);
    long countByPriority(Priority priority);

    @Query("select r.category.name, count(r) from CrimeReport r group by r.category.name order by count(r) desc")
    List<Object[]> countByCategory();

    @Query("select r.city, count(r) from CrimeReport r group by r.city order by count(r) desc")
    List<Object[]> countByCity();

    @Query("select r.status, count(r) from CrimeReport r group by r.status")
    List<Object[]> countGroupedByStatus();

    @Query("select year(r.incidentDate), month(r.incidentDate), count(r) from CrimeReport r where r.incidentDate >= :from group by year(r.incidentDate), month(r.incidentDate) order by year(r.incidentDate), month(r.incidentDate)")
    List<Object[]> countMonthly(@Param("from") LocalDate from);
}

