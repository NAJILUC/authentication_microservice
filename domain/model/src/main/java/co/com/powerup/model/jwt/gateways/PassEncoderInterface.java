package co.com.powerup.model.jwt.gateways;

import co.com.powerup.model.user.User;

public interface PassEncoderInterface {
    boolean validatePassword(String rawPassword, User user);
    String encodePassword(String rawPassword);
}
