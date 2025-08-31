package co.com.powerup.usecase.exception;

import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import co.com.powerup.usecase.utils.FieldValidationError;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class BasicValidationException extends RuntimeException {
    private final List<FieldValidationError> errors;

    public BasicValidationException(List<ErrorCodeEnum> errors) {
        super("Validation failed");
        this.errors = errors.stream()
                .map(FieldValidationError::new)
                .toList();
    }

}
