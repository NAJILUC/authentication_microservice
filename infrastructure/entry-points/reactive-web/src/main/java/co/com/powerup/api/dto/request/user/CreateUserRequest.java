package co.com.powerup.api.dto.request.user;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {

    @NotBlank
    private String firstNames;
    @NotBlank
    private String lastNames;
    private LocalDate birthDate;
    private String address;
    @Pattern(regexp = "^[+]?\\d{10,15}$")
    private String phoneNumber;
    @Email
    @NotBlank
    private String email;
    @NotNull
    @DecimalMin(value = "0.0", message = "Base salary must be greater or equal to 0")
    @DecimalMax(value = "15000000.0", message = "Base salary must not exceed 15,000,000")
    private Double baseSalary;
}
