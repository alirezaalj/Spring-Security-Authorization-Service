package ir.alirezaalijani.security.authorization.service.security.config.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration(proxyBeanMethods = false)
public class OAuthServerConfig {

    @Value("${application.info.host:http://localhost:9000}")
    private String application_host;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(form -> form
                .loginPage(WebSecurityConfig.LOGIN_PGE)
                .loginProcessingUrl(WebSecurityConfig.LOGIN_PROCESSING_Url)
                .usernameParameter("uname")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .failureUrl(WebSecurityConfig.LOGIN_FAILURE_URL)
        ).build();

    }

    private static final String registeredClientTable= """
            CREATE TABLE IF NOT EXISTS oauth2_registered_client
            (
                id                            varchar(100)                        NOT NULL,
                client_id                     varchar(100)                        NOT NULL,
                client_id_issued_at           timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
                client_secret                 varchar(200)                        NULL,
                client_secret_expires_at      timestamp                           NULL,
                client_name                   varchar(200)                        NOT NULL,
                client_authentication_methods varchar(1000)                       NOT NULL,
                authorization_grant_types     varchar(1000)                       NOT NULL,
                redirect_uris                 varchar(1000)                       NULL,
                scopes                        varchar(1000)                       NOT NULL,
                client_settings               varchar(2000)                       NOT NULL,
                token_settings                varchar(2000)                       NOT NULL,
                PRIMARY KEY (id)
            );
            """;
    @Bean
    public RegisteredClientRepository registeredClientRepositoryJdbc(JdbcOperations jdbcOperations) {
        jdbcOperations.execute(registeredClientTable);
        return new JdbcRegisteredClientRepository(jdbcOperations);
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder()
          .issuer(application_host)
          .build();
    }
}