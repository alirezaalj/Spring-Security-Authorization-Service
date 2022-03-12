package ir.alirezaalijani.security.authorization.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/authoritarian")
public class AuthoritarianController {
//
//    private final ApplicationConfigData applicationConfigData;
//    private final AuthenticationManager authenticationManager;
//    private final UserService userService;
//    private final LoginAttemptService loginAttemptService;
//    private final JwtUtils jwtUtils;
//    private final ICaptchaService captchaService;
//
//    public AuthoritarianController(ApplicationConfigData applicationConfigData,
//                                   AuthenticationManager authenticationManager,
//                                   UserService userService,
//                                   LoginAttemptService loginAttemptService, JwtUtils jwtUtils,
//                                   @Qualifier("captchaService") ICaptchaService captchaService) {
//        this.applicationConfigData = applicationConfigData;
//        this.authenticationManager = authenticationManager;
//        this.userService = userService;
//        this.loginAttemptService = loginAttemptService;
//        this.jwtUtils = jwtUtils;
//        this.captchaService = captchaService;
//    }
//
//    @GetMapping("/login")
//    public String loginPage(@RequestParam(value = "error", defaultValue = "") String error,
//                            @RequestParam(name = "message", defaultValue = "") String message,
//                            HttpServletRequest request,HttpServletResponse response, Model model) {
//        var token = userService.getAuthCookieValue(request);
//        if (token != null && jwtUtils.validateUserJwtToken(token)) {
//            return "redirect:" + applicationConfigData.getPanelUiInfo().getRedirectLogin() + "?token=" + token;
//        }
//        Cookie cookie = new Cookie(AuthTokenFilterConfig.COOKIE_NAME, "");
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        model.addAttribute("login_user", new LoginRequest());
//        model.addAttribute("error", error);
//        model.addAttribute("page_message", message);
//        return "login";
//    }
//
//    @GetMapping("/password")
//    public String passwordChangePage(@RequestParam(name = "token",defaultValue ="") String token,
//                                     HttpServletResponse response){
//        if (!token.equals("")){
//            Cookie cookie = new Cookie(AuthTokenFilterConfig.COOKIE_NAME, token);
//            cookie.setPath("/");
//            cookie.setMaxAge(1000);
//            cookie.setHttpOnly(true);
//            cookie.setSecure(true);
//            response.addCookie(cookie);
//            return "redirect:/auth/password";
//        }
//        return "redirect:/authoritarian/login";
//    }
//
//    @PostMapping("/login")
//    public String login(@ModelAttribute("login_user") @Valid LoginRequest loginRequest,
//                        BindingResult bindingResult,
//                        HttpServletRequest request,
//                        HttpServletResponse response,
//                        Model model) {
//        if (loginAttemptService.isBlocked(AuthTokenFilterConfig.getClientIP(request))) {
//            log.info("AuthoritarianController ip is in block list");
//            String redirectTo = "/authoritarian/login?error=" + MessageUtil.encodeUtf("شما به مدت 24 ساعت نمیتوانید به سیستم وارد شود.");
//            return "redirect:" + redirectTo;
//        }
//        if (bindingResult.hasErrors()) {
//            return "login";
//        }
//        final String response_v2 = request.getParameter("g-recaptcha-response");
//        if (response_v2 != null && captchaService.processResponse(response_v2)) {
//            Authentication authentication =
//                    authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            String jwt = jwtUtils.generateJwtToken(authentication);
//            userService.updateUserLastLogin(authentication.getName());
//            if (loginRequest.getRemember()) {
//                Cookie cookie = new Cookie(AuthTokenFilterConfig.COOKIE_NAME, jwt);
//                cookie.setPath("/");
//                cookie.setMaxAge(100000);
//                cookie.setHttpOnly(true);
//                cookie.setSecure(true);
//                response.addCookie(cookie);
//            }
//            model.addAttribute("username", loginRequest.getUsername());
//            return "redirect:" + applicationConfigData.getPanelUiInfo().getRedirectLogin() + "?token=" + jwt;
//        }
//        return "redirect:/authoritarian/login?error=" + MessageUtil.encodeUtf("نام کابری یا کلمه عبور اشتباه است.");
//    }
//
//    @GetMapping("signUp")
//    public String signUpPage(Model model, @RequestParam(name = "message", defaultValue = "") String message) {
//        model.addAttribute("sign_up_request", new RegisterRequest());
//        model.addAttribute("message", message);
//        return "register";
//    }
//
//    @PostMapping("signUp")
//    public String signUpUser(@ModelAttribute("sign_up_request")
//                             @Valid RegisterRequest signUpRequest,
//                             BindingResult bindingResult,
//                             HttpServletRequest request, Model model) {
//        if (loginAttemptService.isBlocked(AuthTokenFilterConfig.getClientIP(request))) {
//            log.info("AuthoritarianController ip is in block list");
//            return "error/access-denied-block";
//        }
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//        if (!signUpRequest.getAgreement()) {
//            bindingResult.rejectValue("agreement", "error.sign_up_request", "قبول کردن شرایط و قوانین الزامیست");
//        }
//        if (!signUpRequest.getPassword().equals(signUpRequest.getRePassword())) {
//            bindingResult.rejectValue("rePassword", "error.sign_up_request", "تکرارا کلمه عبور با کلمه عبور همخوانی ندارد.");
//        }
//        final String response_v2 = request.getParameter("g-recaptcha-response");
//        if (response_v2 != null && captchaService.processResponse(response_v2)) {
//            if (userService.accountExistByMobileOrNCode(signUpRequest.getMobile(), signUpRequest.getNationalCode())) {
//                bindingResult.rejectValue("nationalCode", "error.sign_up_request", "کد ملی یا شماره موبایل مطعلق به کابری دیگر است");
//            }
//            if (userService.userExistByUsername(signUpRequest.getUsername())) {
//                bindingResult.rejectValue("username", "error.sign_up_request", "نام کاربری متعلق به کابری دیگر است.");
//            }
//            if (userService.userExistByEmail(signUpRequest.getEmail())) {
//                bindingResult.rejectValue("email", "error.sign_up_request", "ایمیل منعلق به کاربری دیگر است.");
//            }
//            if (bindingResult.hasErrors()) {
//                return "register";
//            }
//            String signUpStepTowToken = userService.generateSmsStepToken(signUpRequest,
//                    new Random().nextInt((9999 - 1000) + 1) + 1000);
//            if (signUpStepTowToken != null) {
//                RegisterStepTowRequest signUpStepTowRequest = new RegisterStepTowRequest();
//                signUpStepTowRequest.setToken(signUpStepTowToken);
//                model.addAttribute("step_tow_request", signUpStepTowRequest);
//                model.addAttribute("token_resend", UserVerificationMail.filterHashLink(signUpStepTowToken, UserVerificationMail.CREAT_TYPE));
//                model.addAttribute("mobile_number", signUpRequest.getMobile());
//                return "register-mobile";
//            } else {
//                model.addAttribute("message", "ثیت نام با شکست مواجه شد لطفا دوباره تلاش کنید.");
//            }
//        }
//        return "register";
//    }
//
//    @GetMapping("sms-resend/{token}")
//    public String smsResend(@PathVariable String token,
//                            @RequestParam("mobile") String mobile, Model model) {
//        String signUpStepTowToken = userService.resendSmsCode(token);
//        if (signUpStepTowToken != null) {
//            RegisterStepTowRequest request = new RegisterStepTowRequest();
//            request.setToken(signUpStepTowToken);
//            model.addAttribute("step_tow_request", request);
//            model.addAttribute("token_resend", UserVerificationMail.filterHashLink(signUpStepTowToken, UserVerificationMail.CREAT_TYPE));
//            model.addAttribute("mobile_number", mobile);
//            return "register-mobile";
//        }
//        return "redirect:/authoritarian/signUp?message=" + MessageUtil.encodeUtf("ثیت نام با شکست مواجه شد لطفا دوباره تلاش کنید.");
//    }
//
//    @PostMapping("signUp-step2")
//    public String signUpUserStep2(@ModelAttribute("step_tow_request")
//                                  @Valid RegisterStepTowRequest signUpRequest,
//                                  BindingResult bindingResult, Model model) {
//
//        // TODO: add code is wrong for 3 time
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
//        RegisterRequest openSignUpRequest = userService.validateStepTowSignUp(signUpRequest.getToken(), signUpRequest.getSmsCode());
//        if (openSignUpRequest != null) {
//            User user = userService.openSignUp(openSignUpRequest);
//            if (user != null) {
//                return "openSignUpSuccess";
//            }
//        }
//        bindingResult.rejectValue("smsCode", "error.step_tow_request", "Code was wrong!");
//        return "register";
//    }
//
//    @GetMapping("email-verification/{token}")
//    public String emailVerification(@PathVariable String token) {
//        if (userService.emailVerification(token)) {
//            return "mail/mail-is-verify";
//        }
//        return "error/error-fail-email-verify";
//    }
//
//
//    @GetMapping("forget")
//    public String forgetPasswordPage(Model model, @RequestParam(name = "message", defaultValue = "") String message) {
//        model.addAttribute("pass_forget_request", new ForgetPasswordRequest());
//        model.addAttribute("page_message", message);
//        return "recover-password";
//    }
//
//    @PostMapping("forget")
//    public String forgetPassword(@Valid @ModelAttribute("pass_forget_request") ForgetPasswordRequest forgetPasswordRequest,
//                                 BindingResult bindingResult,
//                                 HttpServletRequest request,
//                                 Model model) {
//        if (bindingResult.hasErrors()) {
//            return "recover-password";
//        }
//        final String response_v2 = request.getParameter("g-recaptcha-response");
//        if (response_v2 != null && captchaService.processResponse(response_v2)) {
//            if (!userService.userExistByUsernameOrEmail(forgetPasswordRequest.getUsername())) {
//                bindingResult.rejectValue("username", "error.pass_forget_request", "نام کاربری یا ایمیل اشتباه است.");
//                return "recover-password";
//            }
//            User user = userService.findUserByUsername(forgetPasswordRequest.getUsername());
//            String[] email = user.getEmail().split("@");
//            String hideEmail = hideEmailBetween(email[0]) + email[1];
//            model.addAttribute("verify_email", hideEmail);
//            model.addAttribute("username_to_reset", user.getUsername());
//            model.addAttribute("resend", false);
//            return "password-reset-mail";
//        }
//        return "redirect:/authoritarian/signUp?message=" + MessageUtil.encodeUtf("باز نشانی کلمه عبور با شکست مواجه شد.");
//    }
//
//    @PostMapping("forget/send")
//    public String sendForgetPassword(@Valid @ModelAttribute("pass_forget_request") ForgetPasswordRequest request
//            , BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "recover-password";
//        }
//        if (!userService.userExistByUsernameOrEmail(request.getUsername())) {
//            bindingResult.rejectValue("username", "error.pass_forget_request", "Username not exist!");
//            return "recover-password";
//        }
//        User user = userService.findUserByUsernameOrEmail(request.getUsername());
//        String[] email = user.getEmail().split("@");
//        String hideEmail = hideEmailBetween(email[0]) + email[1];
//        // do send mail process
//        boolean result = userService.sendPasswordChangeEmail(user, "", "We're sending you this email because you requested a password reset. Click on this link to create a new password");
//        model.addAttribute("verify_email", hideEmail);
//        model.addAttribute("username_to_reset", user.getUsername());
//        model.addAttribute("resend", true);
//        if (result)
//            model.addAttribute("result_message", "ایمیل بازیابی کلمه عبور برای شما ارسال شد. لطفا ایمیل خود را برسی کنید.");
//        else model.addAttribute("result_message", "ارسال ایمیل باز یابی با شکست مواجه شد لطفا دوباره تلاش کنید.");
//        return "password-reset-mail";
//    }
//
//    @GetMapping("reset-password/{token}")
//    public String resetPasswordPage(@PathVariable String token, Model model) {
//        User user = userService.passwordTokenValidation(token);
//        if (user == null) {
//            return "error/token-validation-fail";
//        }
//        model.addAttribute("username_to_update", user.getUsername());
//        model.addAttribute("user_password_token", token);
//        model.addAttribute("password_request_user", PasswordUserTokenRequest.builder().username(user.getUsername()).token(token).build());
//        return "password-change";
//    }
//
//    @PostMapping("reset-password")
//    public String changePassword(@ModelAttribute("password_request") @Valid PasswordUserTokenRequest passwordRequest,
//                                 BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "password-change";
//        }
//        if (!passwordRequest.getNewPassword().equals(passwordRequest.getReNewPassword())) {
//            bindingResult.rejectValue("reNewPassword", "error.password_request", "کلمه عبور هم خواهی ندارد.");
//            return "password-change";
//        }
//        User user = userService.passwordTokenValidation(passwordRequest.getToken());
//        if (user == null) {
//            bindingResult.rejectValue("reNewPassword", "error.password_request", "لینک تغییر کلمه عبور قابل استفاده نمیباشد.");
//            return "password-change";
//        }
//        System.out.println("update password");
//        userService.updateUserPasswordByToken(user, passwordRequest.getNewPassword());
//        return "redirect:/authoritarian/login?error=" + MessageUtil.encodeUtf("باز نشانی کلمه عبور با شکست مواجه شد.");
//    }
//
//    private String hideEmailBetween(String email) {
//        StringBuilder builder = new StringBuilder();
//        if (email.length() > 4) {
//            int lower = (email.length() > 1 ? 1 : 2);
//            for (int i = 0; i < email.length(); i++) {
//                if (i < lower) {
//                    builder.append(email.charAt(i));
//                } else if (i > 2 && i < email.length() - 2) {
//                    builder.append("*");
//                } else {
//                    builder.append(email.charAt(i));
//                }
//            }
//            return builder.toString();
//        } else {
//            for (int i = 0; i < email.length(); i++) {
//                if (i <= 1) {
//                    builder.append(email.charAt(i));
//                } else {
//                    builder.append("*");
//                }
//            }
//            return builder.toString();
//        }
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpServletResponse response) {
//        Cookie cookie = new Cookie(AuthTokenFilterConfig.COOKIE_NAME, "");
//        cookie.setPath("/");
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        return "redirect:/authoritarian/login";
//    }

}
