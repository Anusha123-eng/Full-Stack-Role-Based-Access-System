
package rbac.backend.controller;

import rbac.backend.entity.Permission;
import rbac.backend.entity.Resource;
import rbac.backend.repository.PermissionRepository;
import rbac.backend.repository.ResourceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionRepository permissions;
    private final ResourceRepository resources;

    public PermissionController(
            PermissionRepository permissions,
            ResourceRepository resources) {
        this.permissions = permissions;
        this.resources = resources;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read:permissions')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "permissions", permissions.findAll()
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:permissions')")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Permission permission = permissions.findById(id).orElse(null);

        if (permission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Permission not found"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "permission", permission
        ));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create:permissions')")
    public ResponseEntity<?> create(@RequestBody Permission body) {
        if (body.getResourceDetail() != null
                && body.getResourceDetail().getId() != null) {

            Resource resource = resources
                    .findById(body.getResourceDetail().getId())
                    .orElse(null);

            if (resource == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Resource not found"
                ));
            }

            body.setResourceDetail(resource);
        }

        Permission saved = permissions.save(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Permission created",
                "permission", saved
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update:permissions')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody Permission body) {

        Permission permission = permissions.findById(id).orElse(null);

        if (permission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Permission not found"
            ));
        }

        permission.setName(body.getName());
        permission.setAction(body.getAction());
        permission.setResource(body.getResource());
        permission.setDescription(body.getDescription());

        if (body.getResourceDetail() != null
                && body.getResourceDetail().getId() != null) {

            Resource resource = resources
                    .findById(body.getResourceDetail().getId())
                    .orElse(null);

            if (resource == null) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Resource not found"
                ));
            }

            permission.setResourceDetail(resource);
        }

        Permission saved = permissions.save(permission);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Permission updated",
                "permission", saved
        ));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:permissions')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Permission permission = permissions.findById(id).orElse(null);

        if (permission == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "success", false,
                    "message", "Permission not found"
            ));
        }

        permissions.delete(permission);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Permission deleted"
        ));
    }
}
