package account.security.event;

public class RoleRemovedEvent extends AccountServiceEvent {

    public RoleRemovedEvent(String subject, String objectName, String objectRole) {
        super(subject, "Remove role %s from %s"
                .formatted(roleWoPrefix(objectRole), objectName));
    }
}
