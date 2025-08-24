package co.com.powerup.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(GET("/api/v1/users"), handler::listenGetAllUsers)
                .and(route(GET("/api/v1/users/{id}"), handler::listenGetUserById))
                .andRoute(POST("/api/v1/users"), handler::listenSaveUser)
                .and(route(PUT("/api/v1/users"), handler::listenUpdateUser))
                .andRoute(DELETE("/api/v1/users/{id}"), handler::listenDeleteUser);
    }
}
