package co.com.powerup.api.handlers.auth;

import co.com.powerup.api.dto.request.auth.LoginRequest;
import co.com.powerup.api.dto.response.auth.LoginResponse;
import co.com.powerup.api.handlers.utils.GenericHandler;
import co.com.powerup.api.mapper.auth.AuthMapper;
import co.com.powerup.model.auth.LoginRequestModel;
import co.com.powerup.usecase.security.AuthUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthHandler extends GenericHandler {

    private final AuthUseCase authUseCase;

    protected AuthHandler(Validator validator, AuthUseCase authUseCase) {
        super(validator);
        this.authUseCase = authUseCase;
    }

    public Mono<ServerResponse> listenLogin(ServerRequest request) {
        return request.bodyToMono(LoginRequest.class)
                .flatMap(this::validate)
                .flatMap(dto -> {
                    LoginRequestModel loginRequestModel = AuthMapper.toModel(dto);
                    return authUseCase.loginUser(loginRequestModel);
                })
                .flatMap(loginResponseModel -> {
                    LoginResponse loginResponse = AuthMapper.toResponse(loginResponseModel);
                    return this.okResponse(loginResponse);
                });
    }
}
