package co.com.powerup.api.config;

import co.com.powerup.api.security.JwtReactiveAuthenticationManager;
import co.com.powerup.model.jwt.gateways.TokenInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.authentication.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final TokenInterface tokenInterface;

    public SecurityConfig(TokenInterface tokenInterface) {
        this.tokenInterface = tokenInterface;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            JwtReactiveAuthenticationManager authManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/actuator/**",
                                "/auth/**",
                                "api/v1/login"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .authenticationManager(authManager)
                .build();
    }

    private AuthenticationWebFilter jwtAuthenticationWebFilter() {
        ReactiveAuthenticationManager authManager = authentication -> {
            String token = authentication.getCredentials().toString();
            return tokenInterface.validateToken(token)
                    .map(user -> new UsernamePasswordAuthenticationToken(
                            user, null, List.of()
                    ));
        };

        AuthenticationWebFilter filter = new AuthenticationWebFilter(authManager);
        filter.setServerAuthenticationConverter(new ServerBearerTokenAuthenticationConverter());
        return filter;
    }
}
