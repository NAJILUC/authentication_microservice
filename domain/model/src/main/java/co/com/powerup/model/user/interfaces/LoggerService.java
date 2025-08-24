package co.com.powerup.model.user.interfaces;

public interface LoggerService {
    void info(String message, Object... params);
    void error(String message, Object... params);
}
