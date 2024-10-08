package account.service.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PasswordValidator implements InitializingBean {

    private static final String COMPROMISED_PWDS_FILE_LOCATION = "classpath:compromised_pwds.txt";
    private final Set<String> compromisedPasswords = new HashSet<>();

    private final ResourceLoader resourceLoader;
    private final PasswordEncoder passwordEncoder;

    public PasswordValidator(ResourceLoader resourceLoader, PasswordEncoder passwordEncoder) {
        this.resourceLoader = resourceLoader;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isCompromised(String password) {
        return compromisedPasswords.contains(password);
    }

    public boolean isWrongLength(String password) {
        return password == null || password.length() < 12;
    }

    public boolean isTheSamePassword(String newPassword, String hashOfOldPassword) {
        return passwordEncoder.matches(newPassword, hashOfOldPassword);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Resource resource = resourceLoader.getResource(COMPROMISED_PWDS_FILE_LOCATION);
        List<String> lines = Files.readAllLines(resource.getFile().toPath());
        compromisedPasswords.addAll(lines);
    }
}
