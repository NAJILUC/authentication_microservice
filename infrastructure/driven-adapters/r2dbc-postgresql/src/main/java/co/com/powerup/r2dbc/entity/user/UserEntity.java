package co.com.powerup.r2dbc.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Table("users")
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    @Column
    private Long id;
    private String firstNames;
    private String lastNames;
    private LocalDate birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private Double baseSalary;
    private String password;
    private String identificationNumber;

    private Long roleId;
}
