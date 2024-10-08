package account.controller;

import account.model.SignupModel;
import account.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/empl")
public class EmployeeController {

    private final UserDetailsServiceImpl userDetailsService;

    public EmployeeController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/payment")
    SignupModel getPayment(@AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        //printAuthorizationHeader(request);

        //log.debug(">>> Getting payment for principal: {}", userDetails);
        var userAsSignupModel = userDetailsService.getUserAsSignupModel(userDetails.getUsername());
        log.debug(">>> Found user: {}", userAsSignupModel);
        return userAsSignupModel;
    }

    private void printAuthorizationHeader(HttpServletRequest request) {
        /*log.debug("Request Headers:");
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> log.debug(">>> {}: {}", name, request.getHeader(name)));*/
        String authorizationHeader = request.getHeader("authorization");
        String base64Credentials = authorizationHeader.substring(6); //Trim "Basic " part
        var decode = new String(Base64.getDecoder().decode(base64Credentials));
        log.debug(">>> Decoded authorization: [{}]", decode);
    }
}
