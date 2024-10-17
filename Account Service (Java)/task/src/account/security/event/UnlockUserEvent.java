package account.security.event;

public class UnlockUserEvent extends AccountServiceEvent{
    public UnlockUserEvent(String subject, String object) {
        super(subject, object);
    }
}
