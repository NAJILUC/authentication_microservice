package co.com.powerup.usecase.security;

import co.com.powerup.model.auth.LoginRequestModel;
import co.com.powerup.model.jwt.gateways.PassEncoderInterface;
import co.com.powerup.model.jwt.gateways.TokenInterface;
import co.com.powerup.model.user.gateways.UserRepository;
import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import co.com.powerup.usecase.exception.BasicValidationException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class AuthUseCase {

    private final UserRepository userRepository;
    private final TokenInterface tokenInterface;
    private final PassEncoderInterface passEncoderInterface;

    public Mono<String> loginUser(LoginRequestModel loginRequestModel) {
        return userRepository.findByEmail(loginRequestModel.getEmail())
                .switchIfEmpty(Mono.error(new BasicValidationException(List.of(ErrorCodeEnum.C01USER02))))
                .flatMap(user -> {
                    boolean isValid = passEncoderInterface.validatePassword(loginRequestModel.getPassword(), user);
                    if (!isValid) {
                        return Mono.error(new BasicValidationException(List.of(ErrorCodeEnum.C01LOGI01)));
                    }
                    return Mono.just(user);
                })
                .map(tokenInterface::generateToken);
    }

    public String encodePassword(String password){
        return passEncoderInterface.encodePassword(password);
    }
}