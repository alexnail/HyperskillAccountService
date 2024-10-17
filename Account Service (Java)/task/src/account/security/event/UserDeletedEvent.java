package account.security.event;

public class UserDeletedEvent extends AccountServiceEvent {

    public UserDeletedEvent(String subject, String object) {
        super(subject, object);
    }
}
