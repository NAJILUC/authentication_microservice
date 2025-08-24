package co.com.powerup.usecase.exception;

import co.com.powerup.usecase.utils.FieldValidationError;
import lombok.Data;

import java.util.List;

@Data
public class ValidationException extends RuntimeException {
    private final List<FieldValidationError> errors;

    public ValidationException(List<FieldValidationError> errors) {
        super("Validation failed");
        this.errors = errors;
    }

}
