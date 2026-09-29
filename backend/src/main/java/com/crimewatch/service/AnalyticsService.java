package com.crimewatch.service;

import com.crimewatch.domain.*;
import com.crimewatch.repository.ReportRepository;
import com.crimewatch.web.dto.SystemDtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service @RequiredArgsConstructor
public class AnalyticsService {
    private final ReportRepository reports;

    @Transactional(readOnly = true)
    public AnalyticsResponse analytics() {
        return new AnalyticsResponse(reports.count(), count(ReportStatus.SUBMITTED), count(ReportStatus.UNDER_REVIEW),
            count(ReportStatus.ASSIGNED), count(ReportStatus.UNDER_INVESTIGATION), count(ReportStatus.RESOLVED),
            count(ReportStatus.CLOSED), reports.countByPriority(Priority.HIGH) + reports.countByPriority(Priority.CRITICAL),
            points(reports.countByCategory()), monthly(), points(reports.countByCity()), statusPoints());
    }

    private long count(ReportStatus status) { return reports.countByStatus(status); }
    private List<ChartPoint> points(List<Object[]> rows) { return rows.stream().map(r -> new ChartPoint(String.valueOf(r[0]), ((Number) r[1]).longValue())).toList(); }
    private List<ChartPoint> statusPoints() { return reports.countGroupedByStatus().stream().map(r -> new ChartPoint(((ReportStatus) r[0]).name(), ((Number) r[1]).longValue())).toList(); }
    private List<ChartPoint> monthly() {
        return reports.countMonthly(LocalDate.now().minusMonths(11).withDayOfMonth(1)).stream().map(r -> {
            int year = ((Number) r[0]).intValue(), month = ((Number) r[1]).intValue();
            String name = java.time.Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + year;
            return new ChartPoint(name, ((Number) r[2]).longValue());
        }).toList();
    }
}

