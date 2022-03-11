package ir.alirezaalijani.security.springauthorizationservice.service;



import ir.alirezaalijani.security.springauthorizationservice.domain.request.RegisterRequest;
import ir.alirezaalijani.security.springauthorizationservice.repository.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    // get user or other info
    User findUserByUsername(String username);
    User findUserByUsernameOrEmail(String username);
    String getAuthCookieValue(HttpServletRequest request);
    String getAuthHeaderValue(HttpServletRequest request);
    Integer findIdByUsername(String name);

    // exist check
    boolean userExistByEmail(String email);
    Boolean userExistByUsername(String username);
    Boolean userExistByUsernameOrEmail(String username,String email);

    // add - update
    User openSignUp(RegisterRequest openSignUpRequest);
    void updateUserLastLogin(String name);
    void updateUserPassword(User user, String newPassword);
    void updateUserPasswordByToken(User user, String newPassword);

    // validation info
    User passwordTokenValidation(String token);
    boolean emailVerification(String token);
    boolean tokenCanBeUsed(String id);
    boolean checkUserPasswordMach(User user, String lastPassword);
    RegisterRequest validateStepTowSignUp(String token, Integer smsCode);

    // generate
    boolean sendPasswordChangeEmail(User user,String type,String message);

}
