package ir.alirezaalijani.security.authorization.service.initializers;

import ir.alirezaalijani.security.authorization.service.repository.RoleRepository;
import ir.alirezaalijani.security.authorization.service.repository.UserRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.Role;
import ir.alirezaalijani.security.authorization.service.repository.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class DataSourceInitializer implements AppStartupInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegisteredClientRepository registeredClientRepository;

    public DataSourceInitializer(RoleRepository roleRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 RegisteredClientRepository registeredClientRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public void init() {
        insertRegisteredClient();
        insertRoles();
        insertUser( User.builder()
                .id(0).username("admin").password(passwordEncoder.encode("Admin123!@#"))
                .email("admin@alirezaalijani.ir")
                .emailVerification(true)
                .enable(true)
                .serviceAccess(true)
                .build(), "ROLE_ADMIN", "ROLE_USER");
        insertUser( User.builder()
                .id(0).username("user").password(passwordEncoder.encode("User123!@#"))
                .email("user@alirezaalijani.ir")
                .emailVerification(true)
                .enable(true)
                .serviceAccess(true)
                .build(), "ROLE_USER");
    }

    private void insertRoles() {
        var roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin.isEmpty()) {
            roleRepository.save(new Role(1000, "ROLE_ADMIN"));
        }
        var roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser.isEmpty()) {
            roleRepository.save(new Role(1000, "ROLE_USER"));
        }
    }

    private void insertUser(User user, String... role) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            Set<Role> roles = new HashSet<>();
            for (var r : role) {
                var findRole = roleRepository.findByName(r);
                findRole.ifPresent(roles::add);
            }
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    private void insertRegisteredClient(){
        RegisteredClient registeredClient = RegisteredClient.withId("1")
                .clientId("articles-client")
                .clientSecret(passwordEncoder.encode("secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/articles-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope("articles.read")
                .build();
        registeredClientRepository.save(registeredClient);
    }
}
