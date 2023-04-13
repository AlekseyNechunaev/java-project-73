package hexlet.code.controller;

import hexlet.code.dto.CreateUserDto;
import hexlet.code.dto.GetUserDto;
import hexlet.code.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping
    public List<GetUserDto> getAll() {
        return userService.findAll();
    }

    @GetMapping(path = ID)
    public GetUserDto getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public GetUserDto register(@Valid @RequestBody CreateUserDto dto) {
        return userService.create(dto);
    }

    @PreAuthorize("@authorizationHelper.canAccessUser(#id)")
    @PutMapping(path = ID)
    public GetUserDto update(@PathVariable Long id, @Valid @RequestBody CreateUserDto dto) {
        return userService.update(id, dto);
    }

    @PreAuthorize("@authorizationHelper.canAccessUser(#id)")
    @DeleteMapping(path = ID)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
