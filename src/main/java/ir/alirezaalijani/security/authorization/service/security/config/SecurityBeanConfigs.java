package ir.alirezaalijani.security.authorization.service.security.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

@Configuration
public class SecurityBeanConfigs {

    public static final String[] publicPaths = new String[]{
            "/",
            "/auth/login",
            "/assets/**",
            "/favicon.png",
            "/favicon.ico",
            "/error/**",
            "/api/error/**"};

    public static final String[] registerPaths= new String[]{
            "/register/sign-up",
            "/register/resend/verification-email",
            "/verification/email/{token}",
            "/verification/password/{token}",
            "/forget/password",
            "/forget/username",
            "/forget/change/password",
            "/contact"
    };

    private static final String[] recaptchaPaths= new String[]{
            "/auth/login",
            "/register/sign-up",
            "/register/resend/verification-email",
            "/forget/password",
            "/forget/username",
            "forget/change/password",
            "/contact"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected static PathMatcher pathMatcher() {
        return new AntPathMatcher();
    }

    protected static boolean isPublicPath(HttpServletRequest request) {
        if (request!=null){
            String uri = request.getRequestURI();
            for (var path : publicPaths) {
                if (pathMatcher().match(path, uri)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected static boolean isRecaptchaPath(HttpServletRequest request){
        if (request!=null){
            HttpMethod method= HttpMethod.valueOf(request.getMethod());
            if (method.equals(HttpMethod.POST)){
                String uri = request.getRequestURI();
                for (var path : recaptchaPaths) {
                    if (pathMatcher().match(path, uri)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
    
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public TokenSettings tokenSettings() {
        return TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(30L))
                .build();
    }
}
