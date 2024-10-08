package account.controller;

import account.model.ChangePasswordModel;
import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    public SignupModel signup(@RequestBody @Valid SignupModel input) {
        return userDetailsService.createUser(input);
    }

    @PostMapping("/changepass")
    public ChangePasswordModel changePassword(@RequestBody @Valid ChangePasswordModel input) {
        return userDetailsService.changePassword(input);
    }
}