package co.com.powerup.api.handlers.utils;

import co.com.powerup.api.exceptions.ValidationException;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GenericHandler {

    protected final Validator validator;

    protected GenericHandler(Validator validator) {
        this.validator = validator;
    }

    protected <T> Mono<T> validate(T request) {
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);
        return errors.hasErrors()
                ? Mono.error(new ValidationException(errors))
                : Mono.just(request);
    }

    protected Mono<ServerResponse> okResponse(Object data) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data);
    }
}
