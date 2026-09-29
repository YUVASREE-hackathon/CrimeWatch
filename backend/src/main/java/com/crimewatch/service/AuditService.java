package com.crimewatch.service;

import com.crimewatch.domain.*;
import com.crimewatch.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuditService {
    private final AuditLogRepository logs;
    public void record(AppUser actor, String action, String type, String id, String details) {
        logs.save(AuditLog.builder().actor(actor).action(action).entityType(type).entityId(id).details(details).build());
    }
}

