package ir.alirezaalijani.security.springauthorizationservice.security.config;

import ir.alirezaalijani.security.springauthorizationservice.security.model.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
//         securedEnabled = true,
//         jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenFilterConfig authTokenFilterConfig;
    private final AttemptFilterConfig attemptFilterConfig;
    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService,
                             PasswordEncoder passwordEncoder,
                             AuthTokenFilterConfig authTokenFilterConfig,
                             AttemptFilterConfig attemptFilterConfig) {

        this.userDetailsService=customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authTokenFilterConfig = authTokenFilterConfig;
        this.attemptFilterConfig = attemptFilterConfig;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    private AuthenticationEntryPoint authenticationEntryPoint(){
        return ((request, response, authException) ->  response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized"));
    }

    private AccessDeniedHandler accessDeniedHandler(){
        return ((request, response, accessDeniedException) -> response.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: FORBIDDEN"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
//                .accessDeniedPage("/access-denied")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // public
                .antMatchers(SecurityBeanConfigs.publicPaths()).permitAll()
                // secure
                .antMatchers("/api/**").hasRole("USER")
                .anyRequest().authenticated();
//        http.cors();
        http.addFilterBefore(attemptFilterConfig, SecurityContextPersistenceFilter.class);
        http.addFilterBefore(authTokenFilterConfig, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
