package co.com.powerup.api.mapper.user;

import co.com.powerup.api.dto.request.user.CreateUserRequest;
import co.com.powerup.api.dto.response.user.UserResponse;
import co.com.powerup.model.user.User;

public class UserMapper {

    public static User toModel(CreateUserRequest dto) {
        return User.builder()
                .firstNames(dto.getFirstNames())
                .lastNames(dto.getLastNames())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .address(dto.getAddress())
                .baseSalary(dto.getBaseSalary())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstNames(user.getFirstNames())
                .lastNames(user.getLastNames())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .baseSalary(user.getBaseSalary())
                .build();
    }
}
