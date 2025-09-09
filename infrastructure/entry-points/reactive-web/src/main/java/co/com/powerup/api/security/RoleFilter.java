package co.com.powerup.api.security;

import co.com.powerup.model.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class RoleFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private final Set<Long> allowedRoles;

    public RoleFilter(Set<Long> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    @Override
    public Mono<ServerResponse> filter(ServerRequest request,
                                       HandlerFunction<ServerResponse> next) {
        return request.principal()
                .cast(Authentication.class)
                .flatMap(auth -> {
                    User user = (User) auth.getPrincipal();
                    Long roleId = user.getRoleId();

                    if (allowedRoles.contains(roleId)) {
                        return next.handle(request);
                    } else {
                        return ServerResponse.status(403).build();
                    }
                });
    }
}