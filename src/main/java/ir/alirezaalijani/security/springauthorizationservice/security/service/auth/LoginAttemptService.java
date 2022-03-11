package ir.alirezaalijani.security.springauthorizationservice.security.service.auth;

public interface LoginAttemptService {
    void loginSucceeded(final String key);
    void loginFailed(final String key);
    boolean isBlocked(final String key);
}
