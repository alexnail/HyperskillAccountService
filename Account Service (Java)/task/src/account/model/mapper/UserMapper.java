package account.model.mapper;

import account.entity.Group;
import account.entity.User;
import account.model.SignupModel;
import account.model.UserModel;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDetails toUserModel(User user) {
        return UserModel.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getGroups().stream()
                        .map(g -> new SimpleGrantedAuthority(g.getRole()))
                        .toList())
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
        signupModel.setRoles(saved.getGroups().stream()
                .map(Group::getRole)
                .collect(Collectors.toCollection(TreeSet::new))); // order is checked in tests
        return signupModel;
    }
}
