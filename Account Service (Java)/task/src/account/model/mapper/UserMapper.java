package account.model.mapper;

import account.entity.User;
import account.model.SignupModel;
import account.model.UserModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDetails toUserModel(User user) {
        return UserModel.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public User toEntity(SignupModel signupModel, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(signupModel.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(signupModel.getPassword()));
        user.setName(signupModel.getName());
        user.setLastname(signupModel.getLastname());
        return user;
    }

    public SignupModel toSignupModel(User saved) {
        SignupModel signupModel = new SignupModel();
        signupModel.setId(saved.getId());
        signupModel.setEmail(saved.getEmail());
        signupModel.setName(saved.getName());
        signupModel.setLastname(saved.getLastname());
        return signupModel;
    }
}
