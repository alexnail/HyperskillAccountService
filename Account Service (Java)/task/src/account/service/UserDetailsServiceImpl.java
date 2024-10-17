package account.service;

import account.entity.User;
import account.exception.*;
import account.model.*;
import account.model.mapper.UserMapper;
import account.repository.GroupRepository;
import account.repository.UserRepository;
import account.security.event.*;
import account.service.validator.PasswordValidator;
import account.service.validator.RoleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final RoleValidator roleValidator;
    private final ApplicationEventPublisher eventPublisher;

    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper,
                                  PasswordValidator passwordValidator, PasswordEncoder passwordEncoder,
                                  GroupRepository groupRepository, RoleValidator roleValidator,
                                  ApplicationEventPublisher eventPublisher, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
        this.groupRepository = groupRepository;
        this.roleValidator = roleValidator;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return userMapper.toUserModel(user);
    }

    public SignupModel createUser(SignupModel input) {
        validatePassword(input.getPassword());

        if (userRepository.findByEmailIgnoreCase(input.getEmail()).isPresent()) {
            throw new UserExistsException("User exist!");
        }
        var entity = userMapper.toEntity(input, passwordEncoder);
        entity.setGroups(userRepository.count() == 0
                ? Set.of(groupRepository.findByRole("ROLE_ADMINISTRATOR"))
                : Set.of(groupRepository.findByRole("ROLE_USER")));
        var saved = userRepository.save(entity);

        eventPublisher.publishEvent(new UserCreatedEvent(getPrincipalUsername(), saved.getEmail()));

        return userMapper.toSignupModel(saved);
    }

    public ChangePasswordModel changePassword(ChangePasswordModel input) {
        validatePassword(input.getNewPassword());
        var principal = getPrincipal();
        if (passwordValidator.isTheSamePassword(input.getNewPassword(), principal.getPassword())) {
            throw new SamePasswordException();
        }

        User user = userRepository.findByEmailIgnoreCase(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        user.setPassword(passwordEncoder.encode(input.getNewPassword()));

        userRepository.save(user);

        eventPublisher.publishEvent(new ChangePasswordEvent(getPrincipalUsername(), user.getEmail()));

        input.setEmail(user.getEmail());
        return input;
    }

    private void validatePassword(String password) {
        if (passwordValidator.isWrongLength(password)) {
            throw new PasswordLengthException();
        }
        if (passwordValidator.isCompromised(password)) {
            throw new CompromisedPasswordException();
        }
    }

    public List<SignupModel> getAllUsers() {
        List<SignupModel> users = new ArrayList<>();
        userRepository.findAll().forEach(user -> users.add(userMapper.toSignupModel(user)));
        users.sort(Comparator.comparing(SignupModel::getId));
        return users;
    }

    public Map<String, Object> deleteUser(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (email.equalsIgnoreCase(getPrincipalUsername())) {
            throw new IllegalArgumentException("Can't remove ADMINISTRATOR role!");
        }

        userRepository.delete(user);

        eventPublisher.publishEvent(new UserDeletedEvent(getPrincipalUsername(), user.getEmail()));

        return Map.of("user", email,
                "status", "Deleted successfully!");
    }

    public SignupModel updateRole(UpdateRoleModel body) {
        var user = userRepository.findByEmailIgnoreCase(body.getUser())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        var group = groupRepository.findByRole("ROLE_" + body.getRole());
        if (group == null) {
            throw new RoleNotFoundException();
        }

        if ("REMOVE".equals(body.getOperation())) {
            if (!user.getGroups().contains(group)) {
                throw new IllegalArgumentException("The user does not have a role!");
            }
            if ("ROLE_ADMINISTRATOR".equals(group.getRole())) {
                throw new IllegalArgumentException("Can't remove ADMINISTRATOR role!");
            }
            if (user.getGroups().size() == 1) {
                throw new IllegalArgumentException("The user must have at least one role!");
            }
            user.getGroups().remove(group);
            eventPublisher.publishEvent(new RoleRemovedEvent(getPrincipalUsername(), user.getEmail(), group.getRole()));
        } else if ("GRANT".equals(body.getOperation())) {
            if (roleValidator.isCombinedRole(user.getGroups(), group)) {
                throw new IllegalArgumentException("The user cannot combine administrative and business roles!");
            }
            user.getGroups().add(group);
            eventPublisher.publishEvent(new RoleGrantedEvent(getPrincipalUsername(), user.getEmail(), group.getRole()));
        } else {
            throw new IllegalArgumentException("Operation not supported!");
        }
        var saved = userRepository.save(user);
        return userMapper.toSignupModel(saved);
    }

    public Map<String, Object> updateAccess(UpdateAccessModel body) {
        var user = userRepository.findByEmailIgnoreCase(body.user())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        switch (body.operation()) {
            case "LOCK":
                if (roleValidator.isAdministrator(user)) {
                    throw new IllegalArgumentException("Can't lock the ADMINISTRATOR!");
                }
                user.setLocked(true);
                userRepository.save(user);
                eventPublisher.publishEvent(
                        new LockUserEvent(user.getEmail(), "Lock user %s".formatted(user.getEmail())));
                return Map.of("status", "User %s locked!".formatted(body.user().toLowerCase()));
            case "UNLOCK":
                user.setFailedAttempts(0);
                user.setLocked(false);
                userRepository.save(user);
                eventPublisher.publishEvent(
                        new UnlockUserEvent(getPrincipalUsername(), "Unlock user %s".formatted(user.getEmail())));
                return Map.of("status", "User %s unlocked!".formatted(body.user().toLowerCase()));
        }
        return null;
    }

    private static UserModel getPrincipal() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserModel userModel) {
            return userModel;
        }
        return null; // actually it's "anonymousUser" string, but it can't be cast to UserModel
    }

    private static String getPrincipalUsername() {
        var principal = getPrincipal();
        return principal == null ? null : principal.getUsername();
    }
}
