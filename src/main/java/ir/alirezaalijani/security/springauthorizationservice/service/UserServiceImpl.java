package ir.alirezaalijani.security.springauthorizationservice.service;

import ir.alirezaalijani.security.springauthorizationservice.domain.request.RegisterRequest;
import ir.alirezaalijani.security.springauthorizationservice.repository.RoleRepository;
import ir.alirezaalijani.security.springauthorizationservice.repository.TokenRepository;
import ir.alirezaalijani.security.springauthorizationservice.repository.UserRepository;
import ir.alirezaalijani.security.springauthorizationservice.repository.model.ApplicationToken;
import ir.alirezaalijani.security.springauthorizationservice.repository.model.Role;
import ir.alirezaalijani.security.springauthorizationservice.repository.model.User;
import ir.alirezaalijani.security.springauthorizationservice.security.config.AuthTokenFilterConfig;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.MailSendEvent;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.PasswordChangeMail;
import ir.alirezaalijani.security.springauthorizationservice.security.mail.model.UserVerificationMail;
import ir.alirezaalijani.security.springauthorizationservice.security.service.encryption.DataEncryptor;
import ir.alirezaalijani.security.springauthorizationservice.security.token.SignUpStepTowToken;
import ir.alirezaalijani.security.springauthorizationservice.security.token.UserMailToken;
import ir.alirezaalijani.security.springauthorizationservice.web.error.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService{
    public final static long DAY_IN_MS = 1000 * 60 * 60 * 24;
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DataEncryptor simpleJsonEncryptor;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,
                           @Qualifier("springJsonEncryptor") DataEncryptor simpleJsonEncryptor,
                           ApplicationEventPublisher applicationEventPublisher,
                           TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.simpleJsonEncryptor = simpleJsonEncryptor;
        this.applicationEventPublisher = applicationEventPublisher;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", username)));
    }

    @Override
    public User findUserByUsernameOrEmail(String username) {
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with id %s not fond", username)));
    }

    @Override
    public String getAuthCookieValue(HttpServletRequest request) {
        for (Cookie c:request.getCookies()){
            if (c.getName().equals(AuthTokenFilterConfig.COOKIE_NAME)){
                return c.getValue();
            }
        }
        return null;
    }

    @Override
    public String getAuthHeaderValue(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(AuthTokenFilterConfig.TOKEN_BEARER)) {
            return headerAuth.substring(7);
        }
        return null;
    }

    @Override
    public Integer findIdByUsername(String name) {
        return userRepository.findIdByUsername(name)
                .orElseThrow(()-> new UsernameNotFoundException(String.format("User with id %s not fond",name)));
    }

    @Override
    public boolean userExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean userExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean userExistByUsernameOrEmail(String username,String email) {
        return userRepository.existsByUsernameOrEmail(username,email);
    }

    @Override
    public User openSignUp(RegisterRequest request) {
        Set<Role> roles= Collections.singleton(roleRepository.findByName("USER")
                .orElseThrow(() -> new EntityNotFoundException(this.getClass(),"Role Not found")));
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .emailVerification(false)
                .enable(true)
                .serviceAccess(true)
                .roles(roles).build();
        User newUser = userRepository.save(user);
        UserVerificationMail verificationMail = null;
//                new UserVerificationMail(newUser.getEmail(),
//                        "host",
//                        simpleJsonEncryptor
//                                .encryptDataToToken(new UserMailToken(UUID.randomUUID().toString(), newUser.getUsername()
//                                                , newUser.getEmail()
//                                                , new Date(System.currentTimeMillis() + (5 * DAY_IN_MS))
//                                        )
//                                )
//                );
        applicationEventPublisher.publishEvent(new MailSendEvent(verificationMail));
        return newUser;

    }

    @Override
    public void updateUserLastLogin(String name) {
        userRepository.updateUserLastLogin(name, new Date());
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        sendPasswordChangeEmail(user, "warning", "Warning! Your account password has changed. If you have not made this change, create a new password for your account using the link below.");
    }

    @Override
    public void updateUserPasswordByToken(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User passwordTokenValidation(String token) {
        return null;
    }

    @Override
    public boolean emailVerification(String token) {
        if (token != null) {
            UserMailToken mailToken = simpleJsonEncryptor.decryptTokenToData(token, UserMailToken.class);
            if (mailToken != null && tokenCanBeUsed(mailToken.getId()) && mailToken.getExpiration().compareTo(new Date()) > 0) {
                User user = findUserByUsername(mailToken.getUsername());
                if (user != null && !user.getEmailVerification()) {
                    user.setEmailVerification(true);
                    userRepository.save(user);
                    ApplicationToken applicationToken = ApplicationToken.builder()
                            .id(mailToken.getId())
                            .type("MAIL_VERIFICATION")
                            .useTime(new Date())
                            .username(mailToken.getUsername())
                            .expired(true)
                            .tokenTowFactor(simpleJsonEncryptor.encryptDataToToken(token))
                            .build();
                    tokenRepository.save(applicationToken);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean tokenCanBeUsed(String id) {
        return !tokenRepository.existsByIdAndExpired(id, true);
    }

    @Override
    public boolean checkUserPasswordMach(User user, String lastPassword) {
        return passwordEncoder.matches(user.getPassword(), lastPassword);
    }

    @Override
    public RegisterRequest validateStepTowSignUp(String token, Integer smsCode) {
        if (token != null) {
            SignUpStepTowToken signUpStepTowToken = simpleJsonEncryptor.decryptTokenToData(token, SignUpStepTowToken.class);
            if (signUpStepTowToken != null && signUpStepTowToken.getExpiration().compareTo(new Date()) > 0 && smsCode.equals(signUpStepTowToken.getSmsCod())) {
                return signUpStepTowToken.getRequest();
            }
        }
        return null;
    }

    @Override
    public boolean sendPasswordChangeEmail(User user, String type, String message) {
        PasswordChangeMail userPasswordChangeMail = null;
//                new PasswordChangeMail(user.getEmail(), type,
//                       "host",
//                        user.getUsername(), message,
//                        simpleJsonEncryptor
//                                .encryptDataToToken(new PasswordChangeMailToken(UUID.randomUUID().toString(), user.getUsername()
//                                                , user.getEmail()
//                                                , new Date(System.currentTimeMillis() + (DAY_IN_MS))
//                                        )
//                                )
//                );

        applicationEventPublisher.publishEvent(new MailSendEvent( userPasswordChangeMail));
        return true;
    }

}
