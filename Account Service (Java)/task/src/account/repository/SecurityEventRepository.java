package account.repository;

import account.entity.SecurityEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityEventRepository extends CrudRepository<SecurityEvent, Long> {
}
