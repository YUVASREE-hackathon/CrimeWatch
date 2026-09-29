package com.crimewatch.web.dto;

import com.crimewatch.domain.*;
import java.time.Instant;
import java.util.*;

public final class SystemDtos {
    private SystemDtos() {}
    public record Metric(String label, long value) {}
    public record ChartPoint(String name, long value) {}
    public record AnalyticsResponse(
        long totalReports, long submitted, long underReview, long assigned,
        long underInvestigation, long resolved, long closed, long highPriority,
        List<ChartPoint> categories, List<ChartPoint> monthly,
        List<ChartPoint> locations, List<ChartPoint> statuses
    ) {}
    public record NotificationView(Long id, String message, boolean read, String reportId, Instant createdAt) {}
    public record OfficerView(Long id, String fullName, String email, String badgeNumber, String department, String rank, boolean available) {}
    public record AuditView(Long id, String actor, String action, String entityType, String entityId, String details, Instant createdAt) {}
}
