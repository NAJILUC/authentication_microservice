package co.com.powerup.api.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String firstNames;
    private String lastNames;
    private String address;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private Double baseSalary;
}
