package ir.alirezaalijani.security.authorization.service.web;

import ir.alirezaalijani.security.authorization.service.config.ApplicationConfigData;
import ir.alirezaalijani.security.authorization.service.domain.request.LoginRequest;
import ir.alirezaalijani.security.authorization.service.security.captcha.ICaptchaService;
import ir.alirezaalijani.security.authorization.service.security.service.auth.AuthTokenService;
import ir.alirezaalijani.security.authorization.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("auth")
public class AuthenticationController {

    private final UserService userService;
    private final AuthTokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final ICaptchaService captchaService;
    private final ApplicationConfigData applicationConfigData;
    public AuthenticationController(UserService userService,
                                    @Qualifier("authTokenServiceBean") AuthTokenService tokenService,
                                    AuthenticationManager authenticationManager,
                                    @Qualifier("captchaService") ICaptchaService captchaService,
                                    ApplicationConfigData configData) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.captchaService = captchaService;
        this.applicationConfigData=configData;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", defaultValue = "") String error,
                            @RequestParam(name = "message", defaultValue = "") String message,Model model) {
        model.addAttribute("login_user", new LoginRequest());
        model.addAttribute("error", error);
        model.addAttribute("message",message);
        return ViewNames.LOGIN;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("login_user") @Valid LoginRequest loginRequest,
                        BindingResult bindingResult,
                        HttpServletRequest request,
                        Model model) {
        if (bindingResult.hasErrors()) {
            return ViewNames.LOGIN;
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
                var redirectUrl=this.applicationConfigData.sec_login_redirect_url.replace("{token}",tokenOp.get());
                return "redirect:".concat(redirectUrl);
            }
        }else {
            model.addAttribute("error","Recaptcha Validation Required!");
        }
        return ViewNames.LOGIN;
    }

    private static class ViewNames{
        private static final String LOGIN= "login/login";
    }
}
