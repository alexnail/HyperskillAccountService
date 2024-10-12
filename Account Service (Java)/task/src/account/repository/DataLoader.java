package account.repository;

import account.entity.Group;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader {

    private final GroupRepository groupRepository;

    public DataLoader(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
        createRoles();
    }

    private void createRoles() {
        log.info(">>> Creating roles");
        try {
            groupRepository.save(new Group("ROLE_ADMINISTRATOR"));
            groupRepository.save(new Group("ROLE_USER"));
            groupRepository.save(new Group("ROLE_ACCOUNTANT"));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
