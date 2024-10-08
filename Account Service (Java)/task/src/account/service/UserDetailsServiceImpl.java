package account.service;

import account.entity.User;
import account.exception.UserExistsException;
import account.model.SignupModel;
import account.model.mapper.UserMapper;
import account.repository.UserDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    private final UserDAO userDao;

    public UserDetailsServiceImpl(UserDAO userDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return userMapper.toUserModel(user);
    }


    public SignupModel createUser(SignupModel input) {
        if (userDao.findByUserName(input.getEmail()).isPresent()) {
            throw new UserExistsException("User exists!");
        }
        var entity = userMapper.toEntity(input);
        var saved = userDao.save(entity);
        return userMapper.toSignupModel(saved);
    }
}
