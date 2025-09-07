package co.com.powerup.r2dbc.repositories.jwt;

import co.com.powerup.model.jwt.gateways.TokenInterface;
import co.com.powerup.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenAdapter  implements TokenInterface {

    private final Key key;
    private final long expiration;

    public JwtTokenAdapter(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT Secret must be at least 32 characters long");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getIdentificationNumber())
                .claim("roleId", user.getRolId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public Mono<User> validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            User user = new User();
            user.setIdentificationNumber(claims.getSubject());
            user.setRolId((Long) claims.get("roleId"));
            return Mono.just(user);

        } catch (JwtException | IllegalArgumentException e) {
            return Mono.error(new RuntimeException("Invalid or expired JWT token"));
        }
    }
}
