package account.controller;

import account.exception.UserExistsException;
import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

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
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "message", "User exist!",
                "path", "/api/auth/signup"
                );
        return new ResponseEntity(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity bindException(BindException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }}
