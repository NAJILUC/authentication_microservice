package co.com.powerup.api.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdUserRequest {

    private String firstNames;
    private String lastNames;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
}
