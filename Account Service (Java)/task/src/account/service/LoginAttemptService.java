package account.service;

import org.springframework.transaction.annotation.Transactional;

public interface LoginAttemptService {
    @Transactional
    void failedAttempt(String principal, String requestURI);

    @Transactional
    void resetFailedAttempts(String principal);
}
