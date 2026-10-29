package rbac.backend.controller;

import rbac.backend.entity.AuditLog;
import rbac.backend.repository.AuditLogRepository;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/audit")
public class AuditLogController {

    private final AuditLogRepository auditLogs;

    public AuditLogController(AuditLogRepository auditLogs) {
        this.auditLogs = auditLogs;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read:audit')")
    public ResponseEntity<?> getLogs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {

        if (page < 1 || limit < 1 || limit > 500) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Invalid page or limit"));
        }

        Specification<AuditLog> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (action != null && !action.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("action")),
                        "%" + action.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (userId != null) {
                predicates.add(cb.equal(root.get("userId"), userId));
            }
            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("timestamp"), startDate));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("timestamp"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(
                page - 1, limit,
                Sort.by(Sort.Direction.DESC, "timestamp"));

        Page<AuditLog> result =
                auditLogs.findAll(specification, pageable);

        Map<String, Object> pagination = Map.of(
                "total", result.getTotalElements(),
                "page", page,
                "limit", limit,
                "pages", result.getTotalPages());

        return ResponseEntity.ok(Map.of(
                "success", true,
                "logs", result.getContent(),
                "pagination", pagination));
    }

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('read:audit')")
    public ResponseEntity<?> getStats() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "stats", Map.of(
                        "totalLogs", auditLogs.count(),
                        "successLogs", auditLogs.countByStatus("success"),
                        "failedLogs", auditLogs.countByStatus("failed"),
                        "deniedLogs", auditLogs.countByStatus("denied")
                )));
    }
}