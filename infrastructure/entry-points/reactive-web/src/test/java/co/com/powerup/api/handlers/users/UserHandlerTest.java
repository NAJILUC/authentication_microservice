package co.com.powerup.api.handlers.users;

import co.com.powerup.api.dto.request.user.CreateUserRequest;
import co.com.powerup.api.dto.response.user.UserResponse;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
class UserHandlerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserHandler userHandler;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // montar un router con los endpoints que usa el handler
        RouterFunction<ServerResponse> router = route(GET("/api/v1/users"), userHandler::listenGetAllUsers)
                .andRoute(GET("/api/v1/users/{id}"), userHandler::listenGetUserById)
                .andRoute(POST("/api/v1/users"), userHandler::listenCreateUser);

        webTestClient = WebTestClient.bindToRouterFunction(router).build();
    }

    @Test
    void testListenGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setFirstNames("Julian");
        user.setLastNames("Navarro");
        user.setEmail("julian@example.com");
        user.setBaseSalary(1000.0);

        when(userUseCase.getAllUsers()).thenReturn(Flux.just(user));

        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserResponse.class)
                .hasSize(1);
    }

    @Test
    void testListenGetUserById_Found() {
        User user = new User();
        user.setId(1L);
        user.setFirstNames("Julian");
        user.setLastNames("Navarro");
        user.setEmail("julian@example.com");
        user.setBaseSalary(1000.0);
        when(userUseCase.getUserById(1L)).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(resp -> {
                    assert resp.getId().equals(1L);
                });
    }

    @Test
    void testListenGetUserById_NotFound() {
        when(userUseCase.getUserById(1L)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/v1/users/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testListenCreateUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setFirstNames("Julian");
        request.setLastNames("Navarro");
        request.setEmail("julian@example.com");
        request.setBaseSalary(1000.0);

        User user = new User();
        user.setId(1L);
        user.setFirstNames("Julian");
        user.setLastNames("Navarro");
        user.setEmail("julian@example.com");
        user.setBaseSalary(1000.0);

        when(userUseCase.createUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponse.class)
                .value(resp -> {
                    assert resp.getId().equals(1L);
                    assert resp.getFirstNames().equals("Julian");
                });
    }
}