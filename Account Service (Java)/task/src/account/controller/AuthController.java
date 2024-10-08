package account.controller;

import account.exception.UserExistsException;
import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/signup")
    SignupModel signup(@RequestBody @Valid SignupModel input) {
        return userDetailsService.createUser(input);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity handleUserExists() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
