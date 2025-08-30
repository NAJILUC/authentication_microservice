package co.com.powerup.usecase.utils;

import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldValidationError {
    private final String code;
    private final String field;
    private final String message;

    public FieldValidationError(ErrorCodeEnum error) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.field = error.getField();
    }
}
