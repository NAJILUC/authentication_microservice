package co.com.powerup.api.routerrest.users;

import co.com.powerup.api.dto.request.user.CreateUserRequest;
import co.com.powerup.api.dto.response.user.UserResponse;
import co.com.powerup.api.handlers.users.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    operation = @Operation(
                            operationId = "createUser",
                            summary = "Create a new user",
                            tags = {"Users"},
                            requestBody = @RequestBody(
                                    description = "User data",
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = CreateUserRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Created user",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    schema = @Schema(implementation = UserResponse.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Bad request"),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.GET,
                    operation = @Operation(
                            operationId = "getAllUsers",
                            summary = "Get all users",
                            tags = {"Users"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Users List",
                                            content = @Content(
                                                    mediaType = "application/json",
                                                    array = @ArraySchema(schema = @Schema(implementation = UserResponse.class))
                                            )
                                    ),
                                    @ApiResponse(responseCode = "500", description = "Internal server error")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler userHandler) {
        return route(GET("/api/v1/users")
                        .and(accept(MediaType.APPLICATION_JSON)),
                userHandler::listenGetAllUsers)
                .and(route(GET("/api/v1/users/{id}")
                                .and(accept(MediaType.APPLICATION_JSON)),
                        userHandler::listenGetUserById))
                .andRoute(POST("/api/v1/users"),
                        userHandler::listenCreateUser);
    }
}
