package ir.alirezaalijani.security.springauthorizationservice.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

@Configuration
public class SecurityBeanConfigs {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected static PathMatcher pathMatcher(){
        return new AntPathMatcher();
    }

    protected static String[] publicPaths(){
        return new String[]
                {"/","/auth/**","/vendors/**","/assets/**","/img/**","/favicon.png","/favicon.ico","/error/**","/api/error/**"};
    }

    protected static boolean isPublicPath(HttpServletRequest request){
        String uri=request.getRequestURI();
        for (var path:publicPaths()){
            if (pathMatcher().match(path,uri)){
                return true;
            }
        }
        return false;
    }

}
