package ir.alirezaalijani.security.authorization.service.security.config;

import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RecaptchaFilterConfig extends OncePerRequestFilter {

    private final ICaptchaService captchaService;

    public RecaptchaFilterConfig(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (SecurityBeanConfigs.isRecaptchaPath(request)){
            final String response_v2 = request.getParameter("g-recaptcha-response");
            if (response_v2 != null && captchaService.processResponse(response_v2)) {
                filterChain.doFilter(request,response);
            }else {
                log.error("Fail recaptcha validating");
                response.setHeader("Location", request.getRequestURI()+"?error=Recaptcha Validation Required!");
                response.setStatus(302);
            }
        }else {
            filterChain.doFilter(request,response);
        }
    }
}
