package rbac.backend.controller;

import rbac.backend.entity.User;
import rbac.backend.entity.Role;
import rbac.backend.entity.Permission;
import rbac.backend.repository.UserRepository;
import rbac.backend.repository.RoleRepository;
import rbac.backend.repository.PermissionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository users, RoleRepository roles,
                          PasswordEncoder passwordEncoder) {
        this.users = users;
        this.roles = roles;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('read:users')")
    public ResponseEntity<?> getAll() {
        List<Map<String, Object>> result = users.findAll()
                .stream().map(this::safeUser).toList();

        return ResponseEntity.ok(Map.of(
                "success", true, "count", result.size(), "users", result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:users')")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        User user = users.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "User not found"));
        }
        return ResponseEntity.ok(Map.of(
                "success", true, "user", safeUser(user)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('create:users')")
    public ResponseEntity<?> create(@RequestBody UserRequest body) {
        if (users.existsByUsername(body.username)
                || users.existsByEmail(body.email)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Username or email already exists"));
        }

        User user = new User();
        user.setUsername(body.username);
        user.setEmail(body.email);
        user.setPassword(passwordEncoder.encode(body.password));
        user.setFullName(body.fullName);
        user.setIsActive(true);

        if (body.roleIds != null) {
            user.setRoles(new HashSet<>(roles.findAllById(body.roleIds)));
        }

        User saved = users.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "User created successfully",
                "user", safeUser(saved)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update:users')")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody UserRequest body) {
        User user = users.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "User not found"));
        }

        if (body.fullName != null) user.setFullName(body.fullName);

        if (body.email != null && !body.email.equals(user.getEmail())) {
            if (users.existsByEmail(body.email)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false, "message", "Email already exists"));
            }
            user.setEmail(body.email);
        }

        if (body.isActive != null) user.setIsActive(body.isActive);

        if (body.roleIds != null) {
            user.setRoles(new HashSet<>(roles.findAllById(body.roleIds)));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "User updated successfully",
                "user", safeUser(users.save(user))));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:users')")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    Authentication authentication) {
        User user = users.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false, "message", "User not found"));
        }

        if (authentication != null &&
                user.getUsername().equals(authentication.getName())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Cannot delete your own account"));
        }

        users.delete(user);
        return ResponseEntity.ok(Map.of(
                "success", true, "message", "User deleted successfully"));
    }

    private Map<String, Object> safeUser(User user) {
        List<Map<String, Object>> roleList = user.getRoles().stream()
                .map(role -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("id", role.getId());
                    map.put("name", role.getName());
                    map.put("description", role.getDescription());
                    return map;
                }).toList();

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("email", user.getEmail());
        map.put("fullName", user.getFullName());
        map.put("isActive", user.getIsActive());
        map.put("roles", roleList);
        return map;
    }

    public static class UserRequest {
        public String username;
        public String email;
        public String password;
        public String fullName;
        public Boolean isActive;
        public List<Long> roleIds;
    }
}