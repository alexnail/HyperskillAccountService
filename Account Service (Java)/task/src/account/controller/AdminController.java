package account.controller;

import account.model.SignupModel;
import account.model.UpdateRoleModel;
import account.service.UserDetailsServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/user/{email}")
    Map<String, Object> deleteUser(@PathVariable("email") String email){
        return userDetailsService.deleteUser(email);
    }

    @PutMapping("/user/role")
    SignupModel updateRole(@RequestBody UpdateRoleModel body){
        return userDetailsService.updateRole(body);
    }
}
