package co.com.powerup.api.handlers.users;

import co.com.powerup.api.dto.request.user.CreateUserRequest;
import co.com.powerup.api.dto.response.user.UserResponse;
import co.com.powerup.api.handlers.utils.GenericHandler;
import co.com.powerup.api.mapper.user.UserMapper;
import co.com.powerup.api.util.ResponseHelper;
import co.com.powerup.model.user.User;
import co.com.powerup.usecase.user.UserUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserHandler extends GenericHandler {

    private final UserUseCase userUseCase;

    protected UserHandler(Validator validator, UserUseCase userUseCase) {
        super(validator);
        this.userUseCase = userUseCase;
    }

    public Mono<ServerResponse> listenGetAllUsers(ServerRequest serverRequest) {
        return userUseCase.getAllUsers()
                .map(UserMapper::toResponse)
                .collectList()
                .flatMap(this::okResponse);
    }

    public Mono<ServerResponse> listenGetUserById(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));

        return userUseCase.getUserById(id)
                .map(UserMapper::toResponse)
                .flatMap(this::okResponse)
                .switchIfEmpty(ResponseHelper.notFound("C01USR01", "User was not found"));
    }

    public Mono<ServerResponse> listenCreateUser(ServerRequest request) {
        return request.bodyToMono(CreateUserRequest.class)
                .flatMap(this::validate)
                .flatMap(dto -> {
                    User user = UserMapper.toModel(dto);
                    return userUseCase.createUser(user);
                })
                .flatMap(user -> {
                    UserResponse userResponse = UserMapper.toResponse(user);
                    return this.okResponse(userResponse);
                });
    }
}
