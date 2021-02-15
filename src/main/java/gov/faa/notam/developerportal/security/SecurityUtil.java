package gov.faa.notam.developerportal.security;

import gov.faa.notam.developerportal.model.entity.UserRole;
import io.jsonwebtoken.Claims;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class SecurityUtil {
    private SecurityUtil() {
    }

    @NonNull
    public static Optional<Long> getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof Claims)
                .map(principal -> (Claims) principal)
                .map(claims -> claims.get("userId", Long.class));
    }

    public static UserRole getCurrentUserRole() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof Claims)
                .map(principal -> (Claims) principal)
                .map(claims -> claims.get("role", String.class))
                .map(UserRole::valueOf)
                .orElse(null);
    }

    public static Optional<String> getCurrentUserEmail() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof Claims)
                .map(principal -> (Claims) principal)
                .map(Claims::getSubject);
    }
}
