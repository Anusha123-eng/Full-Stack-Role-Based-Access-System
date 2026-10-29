package rbac.backend.controller;

import rbac.backend.entity.Resource;
import rbac.backend.repository.ResourceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    private final ResourceRepository resources;

    public ResourceController(ResourceRepository resources) {
        this.resources = resources;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Admin','Manager')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of(
                "success", true, "resources", resources.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Resource resource = resources.findById(id).orElse(null);
        if (resource == null)
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Resource not found"));
        return ResponseEntity.ok(Map.of(
                "success", true, "resource", resource));
    }

    @PostMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> create(@RequestBody Resource body) {
        Resource saved = resources.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true, "message", "Resource created",
                "resource", saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody Resource body) {
        Resource resource = resources.findById(id).orElse(null);
        if (resource == null)
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Resource not found"));

        resource.setName(body.getName());
        resource.setType(body.getType());
        resource.setPath(body.getPath());
        resource.setDescription(body.getDescription());
        resource.setIsActive(body.getIsActive());

        return ResponseEntity.ok(Map.of(
                "success", true, "message", "Resource updated",
                "resource", resources.save(resource)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Resource resource = resources.findById(id).orElse(null);
        if (resource == null)
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Resource not found"));

        resources.delete(resource);
        return ResponseEntity.ok(Map.of(
                "success", true, "message", "Resource deleted"));
    }
}
