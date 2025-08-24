package co.com.powerup.model.user.gateways;

import co.com.powerup.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> save(User User);

    Flux<User> findAll();

    Mono<User> findById(String id);

    Mono<Void> deleteById(String id);
}
