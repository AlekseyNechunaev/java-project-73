package hexlet.code.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WelcomeController.WELCOME_PATH)
public class WelcomeController {
    public static final String WELCOME_PATH = "/welcome";

    @GetMapping
    public String welcome() {
        return "Welcome to Spring";
    }
}
