package ir.alirezaalijani.security.authorization.service.security.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface AuthTokenService {
    Optional<String> generateToken(Authentication authentication);
    Optional<String> getSubject(String token);
    Optional<UserDetails> getUserDetail(String token);
    boolean validateToken(String token);
}
