package account.service.validator;

import account.entity.Group;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

@Component
public class RoleValidator {

    private static final Set<String> BUSINESS_ROLES = Set.of("ROLE_USER", "ROLE_ACCOUNTANT", "ROLE_AUDITOR");
    private static final Set<String> ADMINISTRATIVE_ROLES = Set.of("ROLE_ADMINISTRATOR");

    public boolean isCombinedRole(Set<Group> groups, Group group) {
        return (ADMINISTRATIVE_ROLES.contains(group.getRole())
                && !Collections.disjoint(BUSINESS_ROLES, groups.stream().map(Group::getRole).toList()))
                ||
                (BUSINESS_ROLES.contains(group.getRole())
                && !Collections.disjoint(ADMINISTRATIVE_ROLES, groups.stream().map(Group::getRole).toList())) ;
    }
}
