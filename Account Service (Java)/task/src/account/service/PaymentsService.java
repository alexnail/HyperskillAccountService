package account.service;

import account.model.PaymentInModel;
import account.model.PaymentOutModel;
import account.validation.ValidPeriod;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentsService {
    @Transactional
    void createPayments(@Valid List<PaymentInModel> payments);

    void updatePayment(@Valid PaymentInModel payment);

    List<PaymentOutModel> getPayments(UserDetails user);

    PaymentOutModel getPayment(@ValidPeriod String periodId, UserDetails user);
}
