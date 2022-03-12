package ir.alirezaalijani.security.authorization.service.security.config;


import ir.alirezaalijani.security.authorization.service.security.service.auth.AuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Component
public class AuthTokenFilterConfig extends OncePerRequestFilter {

    public static final String TOKEN_BEARER = "Bearer ";
    public final static String COOKIE_NAME = "Authorization-token";

    private final AuthTokenService authTokenService;
    private final UserDetailsService userDetailsService;

    public AuthTokenFilterConfig(@Qualifier("simpleJwtTokenGenerator") AuthTokenService authTokenService,
                                 @Qualifier("custom") UserDetailsService userDetailsService) {
        this.authTokenService = authTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if (!SecurityBeanConfigs.isPublicPath(request)) {
            log.info("Request is secure {}", request.getRequestURI());
            try {
                parseJwt(request)
                        .ifPresent(token -> {
                            if (authTokenService.validateToken(token)) {
                                authTokenService.getSubject(token).ifPresent(
                                        subject -> {
                                            UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
                                            if (userDetails == null) {
                                                log.debug("Redirect to new login");
                                                response.setHeader("Location", "/auth/login");
                                                response.setStatus(302);
                                            } else {
                                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                                        userDetails, null, userDetails.getAuthorities());
                                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                            }
                                        }
                                );
                            }
                        });
            } catch (Exception e) {
                logger.error("Cannot set user authentication: {}", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(TOKEN_BEARER)) {
            return Optional.of(headerAuth.substring(7));
        }
        String cookieAuth = getAuthCookie(request);
        if (StringUtils.hasText(cookieAuth)) {
            return Optional.of(cookieAuth);
        }
        return Optional.empty();
    }

    private String getAuthCookie(HttpServletRequest request) {
        if (request.getCookies() != null)
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                    .findAny()
                    .map(Cookie::getValue)
                    .orElse(null);
        return null;
    }

}

