package training.ex.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("loginForm")
    public String loginForm() {
        return "loginForm.html";
    }
}
