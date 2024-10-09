package account.repository;

import account.entity.Payment;
import account.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentsRepository extends CrudRepository<Payment, Long> {
    List<Payment> findAllByEmployee(User employee);

    Optional<Payment> findByEmployeeAndPeriod(User user, String period);
}
