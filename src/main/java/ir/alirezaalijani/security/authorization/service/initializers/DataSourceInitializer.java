package ir.alirezaalijani.security.authorization.service.initializers;

import ir.alirezaalijani.security.authorization.service.repository.RoleRepository;
import ir.alirezaalijani.security.authorization.service.repository.UserRepository;
import ir.alirezaalijani.security.authorization.service.repository.model.Role;
import ir.alirezaalijani.security.authorization.service.repository.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSourceInitializer implements AppStartupInitializer {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSourceInitializer(RoleRepository roleRepository,
                                 UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void init() {
        insertRoles();
        insertUser( User.builder()
                .id(0).username("admin").password(passwordEncoder.encode("12345678"))
                .email("alirezaalijani.ir@gmail.com")
                .emailVerification(true)
                .enable(true)
                .serviceAccess(true)
                .build(), "ROLE_ADMIN", "ROLE_USER");
        insertUser( User.builder()
                .id(0).username("user").password(passwordEncoder.encode("12345678"))
                .email("alirezaalijani07@gmail.com")
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
}
