package co.com.powerup.api.mapper.auth;

import co.com.powerup.api.dto.request.auth.LoginRequest;
import co.com.powerup.api.dto.response.auth.LoginResponse;
import co.com.powerup.api.dto.response.user.UserResponse;
import co.com.powerup.model.auth.LoginResponseModel;
import co.com.powerup.model.user.User;
import co.com.powerup.model.auth.LoginRequestModel;

public class AuthMapper {

    public static LoginRequestModel toModel(LoginRequest dto) {
        return LoginRequestModel.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static LoginResponse toResponse(String token) {
        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
