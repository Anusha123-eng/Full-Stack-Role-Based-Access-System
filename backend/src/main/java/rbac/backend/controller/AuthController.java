
package rbac.backend.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import rbac.backend.entity.User;
import rbac.backend.entity.Role;
import rbac.backend.entity.Permission;
import rbac.backend.repository.UserRepository;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@Transactional
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecretKey secretKey;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret}") String jwtSecret) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.username)
                .orElse(null);

        if (user == null ||
                !passwordEncoder.matches(
                        request.password, user.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "success", false,
                            "message", "Invalid username or password"
                    ));
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "success", false,
                            "message", "Account is inactive"
                    ));
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        String token = Jwts.builder()
                .claim("id", user.getId())
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + 86400000
                ))
                .signWith(secretKey)
                .compact();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("token", token);
        response.put("user", userToMap(user));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request) {

        if (request.username == null || request.username.isBlank()
                || request.email == null || request.email.isBlank()
                || request.password == null || request.password.isBlank()
                || request.fullName == null || request.fullName.isBlank()) {

            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "All fields are required"
            ));
        }

        if (userRepository.existsByUsername(request.username)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Username already exists"
            ));
        }

        if (userRepository.existsByEmail(request.email)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Email already exists"
            ));
        }

        User user = new User(
                request.username,
                request.email,
                passwordEncoder.encode(request.password),
                request.fullName,
                true
        );

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "success", true,
                "message", "Registration successful"
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            org.springframework.security.core.Authentication authentication) {

        if (authentication == null
                || !(authentication.getPrincipal() instanceof User)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not authenticated"));
        }

        User user = (User) authentication.getPrincipal();

        return ResponseEntity.ok(Map.of(
                "success", true,
                "user", userToMap(user)
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Logout successful"
        ));
    }

    private Map<String, Object> userToMap(User user) {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("id", user.getId());
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        result.put("fullName", user.getFullName());

        List<Map<String, Object>> roles = new ArrayList<>();

        for (Role role : user.getRoles()) {
            List<Map<String, Object>> permissions = new ArrayList<>();

            for (Permission permission : role.getPermissions()) {
                Map<String, Object> p = new LinkedHashMap<>();
                p.put("action", permission.getAction());
                p.put("resource", permission.getResource());
                permissions.add(p);
            }

            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", role.getId());
            r.put("name", role.getName());
            r.put("permissions", permissions);

            roles.add(r);
        }

        result.put("roles", roles);

        return result;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class RegisterRequest {
        public String username;
        public String email;
        public String password;
        public String fullName;
    }
}
