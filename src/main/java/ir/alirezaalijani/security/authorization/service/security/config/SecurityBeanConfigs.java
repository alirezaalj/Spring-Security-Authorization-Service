package ir.alirezaalijani.security.authorization.service.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class SecurityBeanConfigs {

    protected static final String[] publicPaths = new String[]{
            "/",
            "/auth/login",
            "/assets/**",
            "/favicon.png",
            "/favicon.ico",
            "/error/**",
            "/api/error/**"};

    protected static final String[] registerPaths= new String[]{
            "/register/sign-up",
            "/register/resend/verification-email",
            "/verification/email/{token}",
            "/verification/password/{token}",
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
        String uri = request.getRequestURI();
        for (var path : publicPaths) {
            if (pathMatcher().match(path, uri)) {
                return true;
            }
        }
        return false;
    }

}
