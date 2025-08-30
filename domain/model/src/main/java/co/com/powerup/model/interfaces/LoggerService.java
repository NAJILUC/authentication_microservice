package co.com.powerup.model.interfaces;

public interface LoggerService {
    void info(String message, Object... params);
    void error(String message, Object... params);
}
