package ir.alirezaalijani.security.springauthorizationservice.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@JsonIgnoreProperties(value = {"*"})
@Configuration
@ConfigurationProperties(prefix = "application.security.encryption.jwt")
public class JwtConfigData {
    private String secretKey;
    private Long expirationMs;
}
