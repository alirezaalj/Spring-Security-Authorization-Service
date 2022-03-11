package ir.alirezaalijani.security.springauthorizationservice.security.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ir.alirezaalijani.security.springauthorizationservice.security.JwtConfigData;
import ir.alirezaalijani.security.springauthorizationservice.security.model.CustomUserDetails;
import ir.alirezaalijani.security.springauthorizationservice.security.model.UserData;
import ir.alirezaalijani.security.springauthorizationservice.security.service.encryption.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataJwtTokenGenerator implements  AuthTokenService{

    private final JwtConfigData jwtConfigData;
    private final DataEncryptor jsonEncryptor;
    private static final String dataClimName="data";
    public DataJwtTokenGenerator(JwtConfigData jwtConfigData,
                                 @Qualifier("jasyptJsonEncryptor") DataEncryptor jsonEncryptor) {
        this.jwtConfigData = jwtConfigData;
        this.jsonEncryptor = jsonEncryptor;
    }
    @Override
    public Optional<String> generateToken(Authentication authentication) {
        try {
            CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            UserData data=new UserData(userPrincipal.getUsername(),userPrincipal.getEmail(),roles);
            return Optional.of(Jwts.builder()
                    .setSubject((userPrincipal.getUsername()))
                    .claim(dataClimName, jsonEncryptor.encryptDataToToken(data))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime()+ jwtConfigData.getExpirationMs()))
                    .signWith(SignatureAlgorithm.HS512, jwtConfigData.getSecretKey())
                    .compact());
        }catch (Exception e){
            log.error("fall to generate data token {}",e.getMessage());
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
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(jwtConfigData.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
            UserData userData = jsonEncryptor.decryptTokenToData((String) body.get(dataClimName),UserData.class);
            if (Objects.nonNull(userData)){
                return Optional.of(CustomUserDetails.build(userData));
            }
        }catch (JwtException | ClassCastException e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean validateToken(String token) {
        return false;
    }
}
