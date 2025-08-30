package co.com.powerup.api.routerrest.users;

import co.com.powerup.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = {UserRouterRest.class})
@WebFluxTest
class UserRouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private co.com.powerup.api.handlers.users.UserHandler userHandler;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setFirstNames("Julian");
        sampleUser.setLastNames("Navarro");
        sampleUser.setEmail("julian@example.com");
        sampleUser.setBaseSalary(1000.0);
    }

    @Test
    @DisplayName("POST /api/v1/users - Should create user successfully")
    void createUserShouldReturnUserResponse() {
        // Arrange
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("message", "User created successfully");

        Mockito.when(userHandler.listenCreateUser(Mockito.any()))
                .thenReturn(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(successResponse));

        // Act & Assert
        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sampleUser)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.message").isEqualTo("User created successfully");
    }

    @Test
    @DisplayName("GET /api/v1/users - Should return list of users")
    void getUsersShouldReturnList() {
        Mockito.when(userHandler.listenGetAllUsers(Mockito.any()))
                .thenReturn(ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(List.of(sampleUser)));

        webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1);
    }

    @Test
    @DisplayName("POST /api/v1/users - Should return 400 for invalid request")
    void createUserInvalidShouldReturnBadRequest() {
        Mockito.when(userHandler.listenCreateUser(Mockito.any()))
                .thenReturn(ServerResponse.badRequest().build());

        webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new User()) // vacío -> inválido
                .exchange()
                .expectStatus().isBadRequest();
    }
}