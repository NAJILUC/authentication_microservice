package co.com.powerup.api.util;

import co.com.powerup.model.user.interfaces.LoggerService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerServiceImpl implements LoggerService {

    @Override
    public void info(String message, Object... params) {
        log.info(message, params);
    }

    @Override
    public void error(String message, Object... params) {
        log.error(message, params);
    }
}
