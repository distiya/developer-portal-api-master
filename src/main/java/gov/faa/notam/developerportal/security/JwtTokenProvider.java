package gov.faa.notam.developerportal.security;

import gov.faa.notam.developerportal.model.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
@Component
public class JwtTokenProvider {

    private static final String TOKEN_PREFIX = "bearer ";

    @Value("${security.secret.jwt}")
    private String jwtSecretBase64Encoded;

    @Value("${security.jwt.valid.duration}")
    private Duration jwtValidDuration;

    public String generateToken(User user) {
        Date iat = new Date();
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(iat)
                .setExpiration(new Date(iat.getTime() + jwtValidDuration.toMillis()))
                .addClaims(Map.of("role", user.getRole().toString(), "userId", user.getId()))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.toLowerCase().startsWith(TOKEN_PREFIX)) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Expired or invalid JWT token.");
        }
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        return new UsernamePasswordAuthenticationToken(claims.getBody(), "",
                Stream.of(claims.getBody().get("role", String.class))
                        .map(r -> "ROLE_" + r)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()));
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretBase64Encoded));
    }
}
