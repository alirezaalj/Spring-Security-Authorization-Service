package ir.alirezaalijani.security.springauthorizationservice.web;


import ir.alirezaalijani.security.springauthorizationservice.security.CaptchaConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@Controller
public class HomeController {

    @Value("classpath:static/img/favicon-48.png")
    private Resource faviconIco;

    @GetMapping("/")
    public String index() {
        return "redirect:/auth/login";
    }

    @GetMapping(path = {"favicon.png","favicon.ico"},produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public Resource faviconIco() {
        try {
            return new InputStreamResource(faviconIco.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
