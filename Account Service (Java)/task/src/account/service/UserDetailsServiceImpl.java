package account.service;

import account.entity.User;
import account.exception.CompromisedPasswordException;
import account.exception.PasswordLengthException;
import account.exception.SamePasswordException;
import account.exception.UserExistsException;
import account.model.ChangePasswordModel;
import account.model.SignupModel;
import account.model.UserModel;
import account.model.mapper.UserMapper;
import account.repository.UserDAO;
import account.service.validator.PasswordValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserDAO userDao;

    private final PasswordValidator passwordValidator;

    public UserDetailsServiceImpl(UserDAO userDao, UserMapper userMapper, PasswordValidator passwordValidator) {
        this.userDao = userDao;
        this.userMapper = userMapper;
        this.passwordValidator = passwordValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return userMapper.toUserModel(user);
    }


    public SignupModel createUser(SignupModel input) {
        validatePassword(input.getPassword());

        if (userDao.findByUserName(input.getEmail()).isPresent()) {
            throw new UserExistsException("User exist!");
        }
        var entity = userMapper.toEntity(input);
        var saved = userDao.save(entity);
        return userMapper.toSignupModel(saved);
    }

    public SignupModel getUserAsSignupModel(String username) {
        User user = userDao.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return userMapper.toSignupModel(user);
    }

    public ChangePasswordModel changePassword(ChangePasswordModel input) {
        validatePassword(input.getNewPassword());
        UserModel principal = (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (passwordValidator.isTheSamePassword(input.getNewPassword(), principal.getPassword())) {
            throw new SamePasswordException();
        }
        var affectedRows = userDao.updatePassword(principal.getUsername(), input.getNewPassword());
        if (1 != affectedRows) {
            log.warn("Either less or more than one rows affected while changing password for user: {}, affectedRows: {}",
                    input.getEmail(), affectedRows);
        }
        input.setEmail(principal.getUsername());
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
}
