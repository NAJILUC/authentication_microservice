package co.com.powerup.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String firstNames;
    private String lastNames;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
}
