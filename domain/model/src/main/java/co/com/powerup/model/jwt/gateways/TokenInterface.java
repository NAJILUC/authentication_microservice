package co.com.powerup.model.jwt.gateways;

import reactor.core.publisher.Mono;

public interface TokenInterface {
    Mono<String> generateToken(String email, Long rolId);
}
