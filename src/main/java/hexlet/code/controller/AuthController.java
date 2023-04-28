package hexlet.code.controller;

import com.rollbar.notifier.Rollbar;
import hexlet.code.dto.AuthDto;
import hexlet.code.service.AuthService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final Rollbar rollbar;

    @Autowired
    public AuthController(AuthService authService, @Autowired(required = false) Rollbar rollbar) {
        this.authService = authService;
        this.rollbar = rollbar;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "success authenticate"),
            @ApiResponse(responseCode = "401", description = "error authenticate")
    })
    @PostMapping
    public String login(@RequestBody @Parameter(description = "user data for authentication", required = true)
                        AuthDto authDto) {
        if (rollbar != null) {
            rollbar.debug("test message");
        }
        return authService.authenticate(authDto);
    }
}
