package hexlet.code.controller;

import hexlet.code.dto.AuthDto;
import hexlet.code.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-api-url}" + AuthController.AUTH_PATH)
public class AuthController {
    public static final String AUTH_PATH = "/login";
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public String login(@RequestBody AuthDto authDto) {
        return authService.authenticate(authDto);
    }
}
