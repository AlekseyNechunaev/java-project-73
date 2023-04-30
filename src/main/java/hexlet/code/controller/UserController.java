package hexlet.code.controller;

import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "${base-api-url}" + UserController.USER_PATH)
public class UserController {
    public static final String USER_PATH = "/users";
    public static final String ID = "/{id}";

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get user list", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
    })
    @GetMapping
    public List<GetUserDto> getAll() {
        return userService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success get user by id", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
    })
    @GetMapping(path = ID)
    public GetUserDto getById(@PathVariable @Parameter(description = "id searched user", required = true) Long id) {
        return userService.findById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "success register user", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
            @ApiResponse(responseCode = "422", description = "validation error"),
            @ApiResponse(responseCode = "400", description = "user exist by email")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public GetUserDto register(@Valid @RequestBody @Parameter(description = "data for create new user",
            required = true) CreateUserDto dto) {
        return userService.create(dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success update user", content =
            @Content(mediaType = "application/json", schema = @Schema(implementation = GetUserDto.class))),
            @ApiResponse(responseCode = "422", description = "validation error"),
            @ApiResponse(responseCode = "400", description = "user exist by email")
    })
    @PreAuthorize("@authorizationHelper.canAccessUser(#id)")
    @PutMapping(path = ID)
    public GetUserDto update(@PathVariable @Parameter(description = "id of the user to be updated", required = true)
                             Long id,
                             @Valid @RequestBody @Parameter(description = "data for update user", required = true)
                             CreateUserDto dto) {
        return userService.update(id, dto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success delete user"),
            @ApiResponse(responseCode = "400", description = "illegal delete user")
    })
    @PreAuthorize("@authorizationHelper.canAccessUser(#id)")
    @DeleteMapping(path = ID)
    public void delete(@PathVariable @Parameter(description = "id of the user to be deleted", required = true)
                       Long id) {
        userService.delete(id);
    }
}
