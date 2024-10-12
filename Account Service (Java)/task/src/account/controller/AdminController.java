package account.controller;

import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserDetailsServiceImpl userDetailsService;

    public AdminController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/user/")
    List<SignupModel> getUsers(){
        return userDetailsService.getAllUsers();
    }
}
