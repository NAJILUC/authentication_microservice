package co.com.powerup.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route(GET("/api/v1/users"), userHandler::listenGetAllUsers)
                .and(route(GET("/api/v1/users/{id}"), userHandler::listenGetUserById))
                .andRoute(POST("/api/v1/users"), userHandler::listenSaveUser)
                .and(route(PUT("/api/v1/users/{id}"), userHandler::listenUpdateUserById))
                .andRoute(DELETE("/api/v1/users/{id}"), userHandler::listenDeleteUser);
    }
}
