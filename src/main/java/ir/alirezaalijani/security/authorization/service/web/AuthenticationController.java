package ir.alirezaalijani.security.authorization.service.web;

import ir.alirezaalijani.security.authorization.service.domain.request.LoginRequest;
import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import ir.alirezaalijani.security.authorization.service.service.UserService;
import ir.alirezaalijani.security.authorization.service.security.service.auth.AuthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ICaptchaService captchaService;
    private final ApplicationEventPublisher eventPublisher;
    private final String successRedirectUrl;
    private final String loginTheme;

    public AuthenticationController(UserService userService,
                                    @Qualifier("simpleJwtTokenGenerator") AuthTokenService tokenService,
                                    AuthenticationManager authenticationManager,
                                    @Qualifier("captchaService") ICaptchaService captchaService,
                                    ApplicationEventPublisher eventPublisher,
                                    @Value("${application.security.login.success.redirect-url}") String successRedirectUrl,
                                    @Value("${application.security.login.theme:default}") String loginTheme) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.captchaService = captchaService;
        this.eventPublisher = eventPublisher;
        this.successRedirectUrl=successRedirectUrl;
        this.loginTheme = loginTheme;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", defaultValue = "") String error,
                            @RequestParam(name = "message", defaultValue = "") String message,Model model) {
        model.addAttribute("login_user", new LoginRequest());
        model.addAttribute("error", error);
        return loginPage();
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login_user") @Valid LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return loginPage();
        }
        final String response_v2 = request.getParameter("g-recaptcha-response");
        if (response_v2 != null && captchaService.processResponse(response_v2)) {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            var tokenOp=tokenService.generateToken(authentication);
            if(tokenOp.isPresent()){
                userService.updateUserLastLogin(authentication.getName());
                model.addAttribute("username", loginRequest.getUsername());
                var redirectUrl=this.successRedirectUrl.replace("{token}",tokenOp.get());
                return "redirect:".concat(redirectUrl);
            }
        }
        return "redirect:/";
    }

    private String loginPage(){
        if(this.loginTheme.equalsIgnoreCase("default")){
            return "login/login-default";
        }
        return "login/login-"+loginTheme;
    }
}
