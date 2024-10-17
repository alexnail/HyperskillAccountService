package account.security.event;

import account.entity.SecurityEvent;
import account.model.UpdateAccessModel;
import account.repository.SecurityEventRepository;
import account.service.LoginAttemptService;
import account.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class SecurityEventListener {

    private final SecurityEventRepository eventRepository;

    private final HttpServletRequest httpServletRequest;

    private final LoginAttemptService loginAttemptService;

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityEventListener(SecurityEventRepository eventRepository, HttpServletRequest httpServletRequest,
                                 LoginAttemptService loginAttemptService, UserDetailsServiceImpl userDetailsService) {
        this.eventRepository = eventRepository;
        this.httpServletRequest = httpServletRequest;
        this.loginAttemptService = loginAttemptService;
        this.userDetailsService = userDetailsService;
    }

    //@EventListener
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        log.debug(">>> Processing event {} of type {}", applicationEvent, applicationEvent.getClass().getSimpleName());
    }

    @EventListener
    public void onUserCreateEvent(UserCreatedEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.CREATE_USER);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }

    @EventListener
    public void onUserDeletedEvent(UserDeletedEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.DELETE_USER);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }

    @EventListener
    public void onRoleGrantedEvent(RoleGrantedEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.GRANT_ROLE);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }

    @EventListener
    public void onRoleRemovedEvent(RoleRemovedEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.REMOVE_ROLE);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }

    @EventListener
    public void onChangePasswordEvent(ChangePasswordEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.CHANGE_PASSWORD);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }

    @EventListener
    public void onBadCredentialsEvent(AuthenticationFailureBadCredentialsEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.LOGIN_FAILED);
        var principal = event.getAuthentication().getName();
        entity.setSubject(principal);
        entity.setObject(httpServletRequest.getRequestURI());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);

        loginAttemptService.failedAttempt(principal, httpServletRequest.getRequestURI());
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        loginAttemptService.resetFailedAttempts(event.getAuthentication().getName());
    }

    @EventListener
    public void onAuthorizationDeniedEvent(AuthorizationDeniedEvent event) {
        var authenticatedUser = event.getAuthentication().get().getName();
        if (!"anonymousUser".equals(authenticatedUser)) {
            SecurityEvent entity = new SecurityEvent();
            entity.setDate(LocalDateTime.now());
            entity.setAction(EventName.ACCESS_DENIED);
            entity.setSubject(authenticatedUser);
            entity.setObject(httpServletRequest.getRequestURI());
            entity.setPath(httpServletRequest.getRequestURI());
            eventRepository.save(entity);
        }
    }

    @EventListener
    public void onBruteForceEvent(BruteForceEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.BRUTE_FORCE);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);

        lockUser(event.getSubject());
    }

    private void lockUser(String username) {
        UpdateAccessModel updateAccessModel = new UpdateAccessModel(username, "LOCK");
        //this is not the best solution but it seems I stuck with it
        // and can't send the request to the update access endpoint without providing admin credentials
        userDetailsService.updateAccess(updateAccessModel);
    }

    @EventListener
    public void onLockUserEvent(LockUserEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.LOCK_USER);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath("/api/admin/user/access");
        eventRepository.save(entity);
    }

    @EventListener
    public void onUnlockUserEvent(UnlockUserEvent event) {
        SecurityEvent entity = new SecurityEvent();
        entity.setDate(LocalDateTime.now());
        entity.setAction(EventName.UNLOCK_USER);
        entity.setSubject(event.getSubject());
        entity.setObject(event.getObject());
        entity.setPath(httpServletRequest.getRequestURI());
        eventRepository.save(entity);
    }
}
