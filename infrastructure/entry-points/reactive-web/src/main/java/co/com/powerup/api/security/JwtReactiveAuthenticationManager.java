package co.com.powerup.api.security;

import co.com.powerup.model.jwt.gateways.TokenInterface;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenInterface tokenInterface;

    public JwtReactiveAuthenticationManager(TokenInterface tokenInterface) {
        this.tokenInterface = tokenInterface;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        return tokenInterface.validateToken(token)
                .map(user -> {
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority("ROLE_" + user.getRoleId());

                    return new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(authority)
                    );
                });
    }
}
