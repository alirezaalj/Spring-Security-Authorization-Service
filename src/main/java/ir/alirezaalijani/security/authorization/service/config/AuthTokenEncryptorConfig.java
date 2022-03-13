package ir.alirezaalijani.security.authorization.service.config;

import ir.alirezaalijani.security.authorization.service.security.JwtConfigData;
import ir.alirezaalijani.security.authorization.service.security.service.auth.AuthTokenService;
import ir.alirezaalijani.security.authorization.service.security.service.auth.DataJwtTokenGenerator;
import ir.alirezaalijani.security.authorization.service.security.service.auth.SimpleJwtTokenGenerator;
import ir.alirezaalijani.security.authorization.service.security.service.encryption.DataEncryptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthTokenEncryptorConfig {

    private final JwtConfigData jwtConfigData;
    private final DataEncryptor jsonEncryptor;

    public AuthTokenEncryptorConfig(JwtConfigData jwtConfigData,
                                    @Qualifier("jasyptJsonEncryptor") DataEncryptor jsonEncryptor) {
        this.jwtConfigData = jwtConfigData;
        this.jsonEncryptor = jsonEncryptor;
    }

    @Bean("authTokenServiceBean")
    @ConditionalOnProperty(name = "application.security.encryption.jwt.type", havingValue = "simple")
    public AuthTokenService authTokenServiceSimple(){
        return new SimpleJwtTokenGenerator(jwtConfigData);
    }

    @Bean("authTokenServiceBean")
    @ConditionalOnProperty(name = "application.security.encryption.jwt.type", havingValue = "data")
    public AuthTokenService authTokenServiceData(){
        return new DataJwtTokenGenerator(jwtConfigData,jsonEncryptor);
    }
}
