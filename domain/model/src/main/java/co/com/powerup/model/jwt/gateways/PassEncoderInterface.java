package co.com.powerup.model.jwt.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Mono;

public interface PassEncoderInterface {
    User encode(User user);

    Mono<Boolean> validatePassword(String password, User user);
}
