package co.com.powerup.api.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.Errors;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidationException extends RuntimeException {
    private final Errors errors;

    public ValidationException(Errors errors) {
        super("Validation failed");
        this.errors = errors;
    }

}
