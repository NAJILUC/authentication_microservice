package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.exception.ValidationException;
import co.com.powerup.usecase.utils.FieldValidationError;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UserUseCase {

    private final UserRepository userRepository;

    public Mono<User> saveUser(User user) {
        return validateUser(user)
                .flatMap(validDto -> userRepository.save(user));
    }

    public Mono<User> updateUserById(String id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> this.validateAndUpdate(existingUser, user))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id);
    }

    private Mono<User> validateUser(User user) {
        List<FieldValidationError> errors = new ArrayList<>();

        if (user.getFirstNames() == null || user.getFirstNames().isEmpty()) {
            errors.add(new FieldValidationError("firstNames", "First Names cannot be empty"));
        }
        if (user.getLastNames() == null || user.getLastNames().isEmpty()) {
            errors.add(new FieldValidationError("lastNames", "Last Names cannot be empty"));
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            errors.add(new FieldValidationError("email", "Invalid email format"));
        }
        if (user.getBaseSalary() == null) {
            errors.add(new FieldValidationError("baseSalary", "Base Salary cannot be empty"));
        } else if (user.getBaseSalary() <= 0 || user.getBaseSalary() > 15000000) {
            errors.add(new FieldValidationError("baseSalary", "Base Salary must be between 0 and 1500000"));
        }

        if (!errors.isEmpty()) {
            return Mono.error(new ValidationException(errors));
        }

        return this.validateEmailUnique(user);
    }

    private Mono<User> validateAndUpdate(User existingUser, User newUser) {

        if (newUser.getEmail() != null && !newUser.getEmail().contains("@")) {
            return Mono.error(new IllegalArgumentException("Email no válido"));
        }

        if (newUser.getLastNames() != null && !newUser.getLastNames().isBlank() &&
                !existingUser.getLastNames().equalsIgnoreCase(newUser.getLastNames())) {
            existingUser.setLastNames(newUser.getLastNames());
        }

        if (newUser.getFirstNames() != null && !newUser.getFirstNames().isBlank() &&
                !existingUser.getFirstNames().equalsIgnoreCase(newUser.getFirstNames())) {
            existingUser.setFirstNames(newUser.getFirstNames());
        }

        if (newUser.getEmail() != null && !newUser.getEmail().isBlank() &&
                !existingUser.getEmail().equalsIgnoreCase(newUser.getEmail())) {
            existingUser.setEmail(newUser.getEmail());
        }

        if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isBlank() &&
                existingUser.getPhoneNumber().equalsIgnoreCase(newUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(newUser.getPhoneNumber());
        }

        return userRepository.save(existingUser);
    }

    private Mono<User> validateEmailUnique(User user) {
        if (user.getEmail() == null) {
            return Mono.just(user);
        }

        return this.checkEmailExists(user.getEmail(), String.valueOf(user.getId()))
                .flatMap(exists -> {
                    if (exists.equals(Boolean.TRUE)) {
                        return Mono.error(new ValidationException(
                                List.of(new FieldValidationError("email", "Email already exists"))
                        ));
                    }
                    return Mono.just(user);
                });
    }

    public Mono<Boolean> checkEmailExists(String email, String currentUserId) {
        return userRepository.findByEmail(email)
                .map(user -> !String.valueOf(user.getId()).equals(currentUserId))
                .defaultIfEmpty(false);
    }
}
