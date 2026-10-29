package rbac.backend.controller;

import rbac.backend.entity.Role;
import rbac.backend.repository.RoleRepository;
import rbac.backend.repository.PermissionRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleRepository roles;
    private final PermissionRepository permissions;

    public RoleController(RoleRepository roles,
                          PermissionRepository permissions) {
        this.roles = roles;
        this.permissions = permissions;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read:roles')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of(
                "success", true, "roles", roles.findAll()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:roles')")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Role role = roles.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Role not found"));
        }
        return ResponseEntity.ok(Map.of("success", true, "role", role));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create:roles')")
    public ResponseEntity<?> create(@RequestBody RoleRequest body) {
        Role role = new Role();
        role.setName(body.name);
        role.setDescription(body.description);
        role.setLevel(body.level);

        if (body.permissionIds != null) {
            role.setPermissions(new HashSet<>(
                    permissions.findAllById(body.permissionIds)));
        }

        Role saved = roles.save(role);
        return ResponseEntity.status(201).body(Map.of(
                "success", true, "message", "Role created", "role", saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update:roles')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody RoleRequest body) {
        Role role = roles.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Role not found"));
        }

        if (body.name != null) role.setName(body.name);
        if (body.description != null) role.setDescription(body.description);
        if (body.level != null) role.setLevel(body.level);

        if (body.permissionIds != null) {
            role.setPermissions(new HashSet<>(
                    permissions.findAllById(body.permissionIds)));
        }

        return ResponseEntity.ok(Map.of(
                "success", true, "message", "Role updated",
                "role", roles.save(role)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:roles')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Role role = roles.findById(id).orElse(null);
        if (role == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "Role not found"));
        }

        roles.delete(role);
        return ResponseEntity.ok(Map.of(
                "success", true, "message", "Role deleted"));
    }

    public static class RoleRequest {
        public String name;
        public String description;
        public Integer level;
        public List<Long> permissionIds;
    }
}
