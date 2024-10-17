package account.security.event;

public class LockUserEvent extends AccountServiceEvent{
    public LockUserEvent(String subject, String object) {
        super(subject, object);
    }
}
