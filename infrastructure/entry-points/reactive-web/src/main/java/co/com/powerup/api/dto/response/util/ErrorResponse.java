package co.com.powerup.api.dto.response.util;

import co.com.powerup.usecase.utils.FieldValidationError;

import java.util.List;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final List<FieldValidationError> errors;

    public ErrorResponse(String code, String message, List<FieldValidationError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public List<FieldValidationError> getErrors() { return errors; }

    public static class FieldError {
        private final String field;
        private final String error;

        public FieldError(String field, String error) {
            this.field = field;
            this.error = error;
        }

        public String getField() { return field; }
        public String getError() { return error; }
    }
}