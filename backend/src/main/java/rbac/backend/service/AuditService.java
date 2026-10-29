
package rbac.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import rbac.backend.entity.AuditLog;
import rbac.backend.repository.AuditLogRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditService(
            AuditLogRepository auditLogRepository,
            ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    public void logAudit(
            HttpServletRequest request,
            HttpServletResponse response,
            String action,
            String resource) {

        try {
            AuditLog auditLog = new AuditLog();

            Object userId = request.getAttribute("userId");

            if (userId instanceof Number) {
                auditLog.setUserId(((Number) userId).longValue());
            }

            auditLog.setAction(action);
            auditLog.setResource(
                    resource != null ? resource : request.getRequestURI()
            );

            int statusCode = response.getStatus();

            auditLog.setStatus(
                    statusCode >= 200 && statusCode < 300
                            ? "success"
                            : "failed"
            );

            auditLog.setIpAddress(request.getRemoteAddr());
            auditLog.setUserAgent(request.getHeader("User-Agent"));

            Map<String, Object> details = new LinkedHashMap<>();
            details.put("method", request.getMethod());
            details.put("path", request.getRequestURI());
            details.put("query", request.getQueryString());
            details.put("statusCode", statusCode);

            auditLog.setDetails(
                    objectMapper.writeValueAsString(details)
            );

            auditLog.setTimestamp(LocalDateTime.now());

            auditLogRepository.save(auditLog);

        } catch (Exception error) {
            System.err.println("Audit log error: " + error.getMessage());
        }
    }

    public void logAccess(
            Long userId,
            String action,
            String resource,
            String status,
            Map<String, Object> details) {

        try {
            AuditLog auditLog = new AuditLog();

            auditLog.setUserId(userId);
            auditLog.setAction(action);
            auditLog.setResource(resource);
            auditLog.setStatus(status);

            Map<String, Object> safeDetails =
                    details != null ? details : new LinkedHashMap<>();

            auditLog.setDetails(
                    objectMapper.writeValueAsString(safeDetails)
            );

            auditLog.setTimestamp(LocalDateTime.now());

            auditLogRepository.save(auditLog);

        } catch (Exception error) {
            System.err.println(
                    "Audit logging failed: " + error.getMessage()
            );
        }
    }
}
