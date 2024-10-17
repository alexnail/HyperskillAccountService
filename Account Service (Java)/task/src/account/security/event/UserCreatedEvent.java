package account.security.event;

public class UserCreatedEvent extends AccountServiceEvent {

    public UserCreatedEvent(String subject, String object) {
        super(subject, object);
    }
}
