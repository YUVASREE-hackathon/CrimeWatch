package com.crimewatch.web;

import com.crimewatch.domain.*;
import com.crimewatch.repository.*;
import com.crimewatch.service.*;
import com.crimewatch.web.dto.AuthDtos.UserView;
import com.crimewatch.web.dto.ReportDtos.CategoryView;
import com.crimewatch.web.dto.SystemDtos.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequiredArgsConstructor
public class SystemController {
    private final AnalyticsService analytics;
    private final CategoryRepository categories;
    private final NotificationRepository notifications;
    private final UserRepository users;
    private final OfficerRepository officers;
    private final AuditLogRepository audits;
    private final CurrentUserService current;

    @GetMapping("/api/analytics/summary") AnalyticsResponse analytics() { return analytics.analytics(); }
    @GetMapping("/api/analytics/categories") List<ChartPoint> categoryAnalytics() { return analytics.analytics().categories(); }
    @GetMapping("/api/analytics/monthly") List<ChartPoint> monthlyAnalytics() { return analytics.analytics().monthly(); }

    @GetMapping("/api/categories") List<CategoryView> categories() {
        return categories.findAll().stream().filter(CrimeCategory::isActive).map(c -> new CategoryView(c.getId(), c.getName(), c.getDescription())).toList();
    }

    public record CategoryRequest(@NotBlank String name, String description) {}
    @PostMapping("/api/admin/categories") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<CategoryView> addCategory(@Valid @RequestBody CategoryRequest request) {
        var saved = categories.save(CrimeCategory.builder().name(request.name().trim()).description(request.description()).build());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CategoryView(saved.getId(), saved.getName(), saved.getDescription()));
    }

    @GetMapping("/api/notifications") List<NotificationView> notifications() {
        return notifications.findByUserIdOrderByCreatedAtDesc(current.get().getId()).stream()
            .map(n -> new NotificationView(n.getId(), n.getMessage(), n.isReadFlag(), n.getReport() == null ? null : n.getReport().getPublicId(), n.getCreatedAt())).toList();
    }
    @PutMapping("/api/notifications/{id}/read") @Transactional NotificationView read(@PathVariable Long id) {
        var actor = current.get();
        var n = notifications.findById(id).filter(x -> x.getUser().getId().equals(actor.getId()))
            .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
        n.setReadFlag(true); notifications.save(n);
        return new NotificationView(n.getId(), n.getMessage(), true, n.getReport() == null ? null : n.getReport().getPublicId(), n.getCreatedAt());
    }

    @GetMapping("/api/admin/users") @PreAuthorize("hasRole('ADMIN')")
    List<UserView> users() { return users.findAll().stream().map(AuthService::view).toList(); }

    @GetMapping("/api/admin/officers") @PreAuthorize("hasAnyRole('ADMIN','OFFICER')")
    List<OfficerView> officers() { return officers.findAll().stream().map(o -> new OfficerView(o.getUser().getId(), o.getUser().getFullName(), o.getUser().getEmail(), o.getBadgeNumber(), o.getDepartment(), o.getRankName(), o.isAvailable())).toList(); }

    @GetMapping("/api/audit") @PreAuthorize("hasRole('ADMIN')")
    List<AuditView> audit(@RequestParam(defaultValue = "50") int limit) {
        return audits.findAllByOrderByCreatedAtDesc(PageRequest.of(0, Math.min(Math.max(limit, 1), 200))).stream()
            .map(a -> new AuditView(a.getId(), a.getActor() == null ? "System" : a.getActor().getFullName(), a.getAction(), a.getEntityType(), a.getEntityId(), a.getDetails(), a.getCreatedAt())).toList();
    }
}
