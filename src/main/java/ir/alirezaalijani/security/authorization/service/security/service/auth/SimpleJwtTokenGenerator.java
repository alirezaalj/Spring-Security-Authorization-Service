package ir.alirezaalijani.security.authorization.service.security.service.auth;

import io.jsonwebtoken.*;
import ir.alirezaalijani.security.authorization.service.security.JwtConfigData;
import ir.alirezaalijani.security.authorization.service.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class SimpleJwtTokenGenerator implements  AuthTokenService{

    private final JwtConfigData jwtConfigData;

    public SimpleJwtTokenGenerator(JwtConfigData jwtConfigData) {
        this.jwtConfigData = jwtConfigData;
    }

    @Override
    public Optional<String> generateToken(Authentication authentication) {
        try {
            CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
            Set<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            return Optional.of(Jwts.builder()
                    .setSubject(userPrincipal.getUsername())
                    .claim("role", roles)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + jwtConfigData.getExpirationMs()))
                    .signWith(SignatureAlgorithm.HS512, jwtConfigData.getSecretKey())
                    .compact());
        }catch (Exception e){
              log.error("fall to generate token {}",e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getSubject(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(jwtConfigData.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDetails> getUserDetail(String token) {
        return Optional.empty();
    }

    @Override
    public boolean validateToken(String token) {
        if (token!=null){
            try {
                Jwts.parser().setSigningKey(jwtConfigData.getSecretKey()).parseClaimsJws(token);
                return true;
            } catch (SignatureException e) {
                log.error("Invalid JWT signature: {}", e.getMessage());
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
            } catch (ExpiredJwtException e) {
                log.error("JWT token is expired: {}", e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.error("JWT token is unsupported: {}", e.getMessage());
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty: {}", e.getMessage());
            }
        }

        return false;
    }
}
