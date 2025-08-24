package co.com.powerup.config;

import co.com.powerup.api.util.LoggerServiceImpl;
import co.com.powerup.model.user.interfaces.LoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggerConfig {
    @Bean
    public LoggerService loggerService() {
        return new LoggerServiceImpl();
    }
}
