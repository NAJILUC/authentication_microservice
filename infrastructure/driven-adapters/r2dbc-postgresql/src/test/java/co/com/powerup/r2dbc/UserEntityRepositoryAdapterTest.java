package co.com.powerup.r2dbc;

import co.com.powerup.model.user.User;
import co.com.powerup.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserEntityRepositoryAdapterTest {

    @InjectMocks
    UserEntityRepositoryAdapter repositoryAdapter;

    @Mock
    UserEntityRepository repository;

    @Mock
    ObjectMapper mapper;

    private final UserEntity userEntity = UserEntity.builder()
            .id(1L)
            .firstNames("Julian")
            .lastNames("Navarro")
            .birthDate(LocalDate.of(2000, 6, 15))
            .address("Calle n")
            .phoneNumber("55555555")
            .email("test@mail.com")
            .baseSalary(123.45)
            .build();

    @Test
    void mustFindValueById() {

        when(repository.findById(1L)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findById(1L);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Flux<User> result = repositoryAdapter.findByExample(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }

    @Test
    void mustSaveValue() {
        when(repository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.map(userEntity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals(user))
                .verifyComplete();
    }
}
