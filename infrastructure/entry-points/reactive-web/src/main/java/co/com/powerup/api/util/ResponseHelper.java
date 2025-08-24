package co.com.powerup.api.util;

import co.com.powerup.api.dto.response.util.ErrorResponse;
import co.com.powerup.usecase.utils.FieldValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;


public class ResponseHelper {

    public static Mono<ServerResponse> notFound(String code, String message) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "code", code,
                        "message", message
                ));
    }

    public static <T> Mono<ServerResponse> ok(T body) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body);
    }

    public static Mono<ServerResponse> validationError(String code, String message, List<FieldValidationError> errors) {
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new ErrorResponse(code, message, errors));
    }
}