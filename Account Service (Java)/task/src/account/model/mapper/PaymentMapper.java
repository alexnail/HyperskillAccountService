package account.model.mapper;

import account.entity.Payment;
import account.model.PaymentInModel;
import account.model.PaymentOutModel;
import account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final UserRepository userRepository;

    public Payment toEntity(PaymentInModel paymentModel) {
        Payment payment = new Payment();
        payment.setEmployee(userRepository.findByEmailIgnoreCase(paymentModel.getEmployee())
                .orElseThrow(() -> new RuntimeException("Couldn't find user name for payment.")));
        payment.setPeriod(paymentModel.getPeriod());
        payment.setSalary(paymentModel.getSalary());
        return payment;
    }

    public PaymentOutModel toOutModel(Payment payment) {
        return PaymentOutModel.builder()
                .name(payment.getEmployee().getName())
                .lastname(payment.getEmployee().getLastname())
                .period(toYearMonth(payment.getPeriod()))
                .salary(toStringSalary(payment.getSalary()))
                .build();
    }

    private String toStringSalary(Long salary) {
        return "%d dollar(s) %d cent(s)".formatted(salary / 100, salary % 100);
    }

    private YearMonth toYearMonth(String period) {
        return YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy"));
    }
}
