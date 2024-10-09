package account.controller;

import account.service.PaymentsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/api/empl")
public class EmployeeController {

    private final PaymentsService paymentsService;

    public EmployeeController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @GetMapping("/payment")
    public Object getPayments(@RequestParam(name = "period", required = false) String periodId,
                              @AuthenticationPrincipal UserDetails user) {
        if (periodId == null) {
            return paymentsService.getPayments(user);
        }
        return paymentsService.getPayment(periodId, user);
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
