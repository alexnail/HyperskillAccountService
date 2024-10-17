package account.security.event;

public class RoleGrantedEvent extends AccountServiceEvent {

    public RoleGrantedEvent(String subject, String objectName, String objectRole) {
        super(subject, "Grant role %s to %s"
                .formatted(roleWoPrefix(objectRole), objectName));
    }
}
