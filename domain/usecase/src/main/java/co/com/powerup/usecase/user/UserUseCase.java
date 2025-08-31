package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import co.com.powerup.usecase.exception.BasicValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;
    private static final Logger log = Logger.getLogger(UserUseCase.class.getName());

    public Mono<User> createUser(User user) {
        return validateUser(user)
                .flatMap(validDto -> userRepository.save(user)
                        .doOnNext(createdUser ->
                                log.log(Level.INFO, "User created {}", createdUser.getId())
                        )
                );
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    private Mono<User> validateUser(User user) {
        log.info("Validating user");
        return this.validateEmailUnique(user);
    }

    private Mono<User> validateEmailUnique(User user) {
        if (user.getEmail() == null) {
            return Mono.just(user);
        }
        log.info("Validating email");

        return this.checkEmailExists(user.getEmail(), String.valueOf(user.getId()))
                .flatMap(exists -> {
                    if (exists.equals(Boolean.TRUE)) {
                        log.warning("Email already exists");
                        return Mono.error(new BasicValidationException(List.of(ErrorCodeEnum.C01USER01)));
                    }
                    return Mono.just(user);
                });
    }

    public Mono<Boolean> checkEmailExists(String email, String currentUserId) {
        return userRepository.findByEmail(email)
                .map(user -> !String.valueOf(user.getId()).equals(currentUserId))
                .defaultIfEmpty(false);
    }

    public Mono<User> getByIdentificationNumber(String identificationNumber) {
        return userRepository.findByIdentificationNumber(identificationNumber);
    }
}
