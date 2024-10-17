package account.service;

import account.entity.User;
import account.repository.UserRepository;
import account.security.event.BruteForceEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public LoginAttemptServiceImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void failedAttempt(String principal, String requestURI) {
        User user = userRepository.findByEmailIgnoreCase(principal)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        userRepository.save(user);

        if (user.getFailedAttempts() >= MAX_ATTEMPTS) {
            eventPublisher.publishEvent(new BruteForceEvent(principal, requestURI));
        }
    }

    @Override
    public void resetFailedAttempts(String principal) {
        User user = userRepository.findByEmailIgnoreCase(principal)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setFailedAttempts(0);
        userRepository.save(user);
    }
}
