package account.controller;

import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/empl")
public class EmployeeController {

    private final UserDetailsServiceImpl userDetailsService;

    public EmployeeController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/payment")
    SignupModel getPayment(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetailsService.getUserAsSignupModel(userDetails.getUsername());
    }
}
