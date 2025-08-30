package co.com.powerup.api.exceptions;

import co.com.powerup.usecase.enums.errorcodes.ErrorCodeEnum;
import co.com.powerup.usecase.enums.errorcodes.ErrorEnum;
import co.com.powerup.usecase.exception.UserValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            Map<String, Object> body = new HashMap<>();

            if (ex instanceof ValidationException validationException) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

                List<Map<String, String>> errors = validationException.getErrors()
                        .getFieldErrors()
                        .stream()
                        .map(error -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("field", error.getField());
                            map.put("message", error.getDefaultMessage());
                            return map;
                        })
                        .toList();

                body.put("status", HttpStatus.BAD_REQUEST.value());
                body.put("error", "Validation Failed");
                body.put("code", ErrorEnum.VALIDATION_EXCEPTION.getCode());
                body.put("errors", errors);

            } else if (ex instanceof UserValidationException userValidationException) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

                String code = userValidationException.getErrors().isEmpty()
                        ? "UNKNOWN"
                        : userValidationException.getErrors().get(0).getCode();

                List<Map<String, String>> errors = userValidationException.getErrors()
                        .stream()
                        .map(err -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("field", err.getField());
                            map.put("message", err.getMessage());
                            return map;
                        })
                        .toList();

                body.put("status", HttpStatus.BAD_REQUEST.value());
                body.put("error", "Validation Failed");
                body.put("code", code);
                body.put("errors", errors);

            } else {
                exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
                body.put("error", "Unexpected error");
                body.put("code", ErrorEnum.INTERNAL_EXCEPTION.getCode());
            }

            byte[] bytes = objectMapper.writeValueAsBytes(body);
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(bytes))
            );

        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            String fallback = "{\"error\": \"Serialization error\"}";
            return exchange.getResponse().writeWith(
                    Mono.just(exchange.getResponse().bufferFactory().wrap(fallback.getBytes()))
            );
        }
    }
}