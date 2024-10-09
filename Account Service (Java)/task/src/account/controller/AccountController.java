package account.controller;

import account.model.PaymentInModel;
import account.service.PaymentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/acct")
public class AccountController {

    private final PaymentsService paymentsService;

    public AccountController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @PostMapping("/payments")
    public Map<String, Object> createPayments(@RequestBody List<PaymentInModel> payments) {
        paymentsService.createPayments(payments);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping("/payments")
    public Map<String, Object> updatePayment(@RequestBody PaymentInModel payment) {
        paymentsService.updatePayment(payment);
        return Map.of("status", "Updated successfully!");
    }
}
