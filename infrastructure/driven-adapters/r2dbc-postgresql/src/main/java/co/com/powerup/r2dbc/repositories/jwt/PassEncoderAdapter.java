package co.com.powerup.r2dbc.repositories.jwt;

import co.com.powerup.model.jwt.gateways.PassEncoderInterface;
import co.com.powerup.model.jwt.gateways.TokenInterface;
import co.com.powerup.model.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class PassEncoderAdapter implements PassEncoderInterface {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public boolean validatePassword(String rawPassword, User user) {
        return encoder.matches(rawPassword, user.getPassword());
    }

    @Override
    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
