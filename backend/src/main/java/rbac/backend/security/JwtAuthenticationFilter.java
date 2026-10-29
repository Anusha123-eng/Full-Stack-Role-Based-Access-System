
package rbac.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;
import rbac.backend.entity.User;
import rbac.backend.entity.Role;
import rbac.backend.entity.Permission;
import rbac.backend.repository.UserRepository;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final SecretKey secretKey;

    public JwtAuthenticationFilter(
            UserRepository userRepository,
            ObjectMapper objectMapper,
            @Value("${jwt.secret}") String jwtSecret) {

        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.secretKey = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = header.substring(7);

            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            Object idClaim = claims.get("id");
            if (idClaim == null) {
                sendError(response, 401, "Invalid token.");
                return;
            }

            Long userId = Long.valueOf(idClaim.toString());

            User user = userRepository.findById(userId).orElse(null);

            if (user == null) {
                sendError(response, 401, "Invalid token. User not found.");
                return;
            }

            if (!Boolean.TRUE.equals(user.getIsActive())) {
                sendError(response, 403,
                        "Account is inactive. Contact administrator.");
                return;
            }

            Set<SimpleGrantedAuthority> authorities = new HashSet<>();

            for (Role role : user.getRoles()) {
                authorities.add(
                        new SimpleGrantedAuthority("ROLE_" + role.getName())
                );

                for (Permission permission : role.getPermissions()) {
                    String permissionKey =
                            permission.getAction() + ":" +
                                    permission.getResource();

                    authorities.add(
                            new SimpleGrantedAuthority(permissionKey)
                    );
                }
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities
                    );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);

            request.setAttribute("userId", user.getId());

            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            SecurityContextHolder.clearContext();
            sendError(response, 401, "Token expired.");

        } catch (JwtException | IllegalArgumentException e) {
            SecurityContextHolder.clearContext();
            sendError(response, 401, "Invalid token.");
        }
    }

    private void sendError(
            HttpServletResponse response,
            int status,
            String message) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("success", false);
        body.put("message", message);

        objectMapper.writeValue(response.getWriter(), body);
    }
}
