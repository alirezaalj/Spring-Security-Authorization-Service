package ir.alirezaalijani.security.springauthorizationservice.security.model;


import ir.alirezaalijani.security.springauthorizationservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service("custom")
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
           return CustomUserDetails.build(userRepository.findByUsernameOrEmail(s,s)
                    .orElseThrow(()-> new UsernameNotFoundException(String.format("Username {%s} Not found",s))));
    }

}
