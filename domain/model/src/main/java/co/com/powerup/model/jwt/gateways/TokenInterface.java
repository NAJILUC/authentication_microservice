package co.com.powerup.model.jwt.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface TokenInterface {
    String generateToken(User user);
    Mono<User> validateToken(String token);
}
