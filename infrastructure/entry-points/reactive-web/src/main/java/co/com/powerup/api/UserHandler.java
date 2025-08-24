package co.com.powerup.api;

import co.com.powerup.api.dto.request.user.CreateUserRequest;
import co.com.powerup.api.mapper.user.UserMapper;
import co.com.powerup.api.util.ResponseHelper;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.exception.ValidationException;
import co.com.powerup.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserUseCase userUseCase;
    private final Validator validator;

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(userUseCase.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return userUseCase.getUserById(id)
                .flatMap(task -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(task))
                .switchIfEmpty(ResponseHelper.notFound("C01USR01", "User was not found"));
    }

    public Mono<ServerResponse> listenSaveUser(ServerRequest request) {
        return request.bodyToMono(CreateUserRequest.class)
                .flatMap(dto -> {
                    User user = UserMapper.toModel(dto);
                    return userUseCase.saveUser(user)
                            .map(UserMapper::toResponse)
                            .flatMap(resp -> ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(resp));
                })
                .onErrorResume(ValidationException.class, ex ->
                        ResponseHelper.validationError("V01FIE01", "Validation failed", ex.getErrors())
                );
    }

    public Mono<ServerResponse> listenUpdateUserById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(User.class)
                .flatMap(user -> userUseCase.updateUserById(id, user))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user));
    }

    public Mono<ServerResponse> listenDeleteUser(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return userUseCase.deleteUser(id)
                .then(ServerResponse.noContent().build());
    }
}
