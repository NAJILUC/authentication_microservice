package co.com.powerup.usecase.user;

import co.com.powerup.model.user.User;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import co.com.powerup.usecase.exception.BasicValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstNames("Julian");
        testUser.setLastNames("Navarro");
        testUser.setEmail("julian@example.com");
        testUser.setBaseSalary(1000.0);
    }

    @Test
    void createUser_success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());
        when(userRepository.save(testUser)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.createUser(testUser))
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepository).save(testUser);
    }

    @Test
    void createUser_emailAlreadyExists_shouldThrowValidationException() {
        User existingUser = new User();
        existingUser.setId(2L);
        existingUser.setEmail(testUser.getEmail());

        testUser.setId(null); // importante: simular nuevo usuario

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(existingUser));

        StepVerifier.create(userUseCase.createUser(testUser))
                .expectErrorSatisfies(error -> {
                    assert error instanceof BasicValidationException;
                    BasicValidationException ex = (BasicValidationException) error;
                    System.out.println(ex.getErrors());
                    assert ex.getErrors().getFirst().getCode().equals(ErrorCodeEnum.C01USER01.getCode());
                })
                .verify();

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(Flux.just(testUser));

        StepVerifier.create(userUseCase.getAllUsers())
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepository).findAll();
    }

    @Test
    void getUserById_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.getUserById(1L))
                .expectNext(testUser)
                .verifyComplete();

        verify(userRepository).findById(1L);
    }

    @Test
    void checkEmailExists_shouldReturnFalseIfEmailNotFound() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.checkEmailExists(testUser.getEmail(), String.valueOf(testUser.getId())))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void checkEmailExists_shouldReturnTrueIfEmailBelongsToAnotherUser() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail(testUser.getEmail());

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(otherUser));

        StepVerifier.create(userUseCase.checkEmailExists(testUser.getEmail(), String.valueOf(testUser.getId())))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void checkEmailExists_shouldReturnFalseIfEmailBelongsToSameUser() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Mono.just(testUser));

        StepVerifier.create(userUseCase.checkEmailExists(testUser.getEmail(), String.valueOf(testUser.getId())))
                .expectNext(false)
                .verifyComplete();
    }
}
