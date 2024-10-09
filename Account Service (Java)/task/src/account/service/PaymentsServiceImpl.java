package account.service;

import account.entity.Payment;
import account.entity.User;
import account.model.PaymentInModel;
import account.model.PaymentOutModel;
import account.model.mapper.PaymentMapper;
import account.repository.PaymentsRepository;
import account.repository.UserRepository;
import account.validation.ValidPeriod;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

@Service
@Validated
public class PaymentsServiceImpl implements PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final UserRepository userRepository;
    private final PaymentMapper paymentMapper;

    public PaymentsServiceImpl(PaymentsRepository paymentsRepository, PaymentMapper paymentMapper,
                               UserRepository userRepository) {
        this.paymentsRepository = paymentsRepository;
        this.paymentMapper = paymentMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void createPayments(@Valid List<PaymentInModel> payments) {
        List<Payment> entities = payments.stream().map(paymentMapper::toEntity).toList();
        paymentsRepository.saveAll(entities);
    }

    @Override
    public void updatePayment(@Valid PaymentInModel inModel) {
        var user = getUser(inModel.getEmployee());
        var payment = paymentsRepository.findByEmployeeAndPeriod(user, inModel.getPeriod())
                .orElseThrow(getPaymentNotFoundExceptionSupplier(user.getEmail(), inModel.getPeriod()));
        payment.setSalary(inModel.getSalary());
        paymentsRepository.save(payment);
    }

    @Override
    public List<PaymentOutModel> getPayments(UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());
        return paymentsRepository.findAllByEmployee(user)
                .stream().map(paymentMapper::toOutModel)
                .sorted(Comparator.comparing(PaymentOutModel::getPeriod).reversed())
                .toList();
    }

    @Override
    public PaymentOutModel getPayment(@ValidPeriod String period, UserDetails userDetails) {
        User user = getUser(userDetails.getUsername());
        return paymentMapper.toOutModel(
                paymentsRepository.findByEmployeeAndPeriod(user, period)
                        .orElseThrow(getPaymentNotFoundExceptionSupplier(user.getEmail(), period))
        );
    }

    private static Supplier<RuntimeException> getPaymentNotFoundExceptionSupplier(String userEmail, String period) {
        return () -> new RuntimeException("Payment not found for user %s and period %s."
                .formatted(userEmail, period));
    }

    private User getUser(String username) {
        return userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
    }
}
