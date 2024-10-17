package account.security.event;

public class ChangePasswordEvent extends AccountServiceEvent {

    public ChangePasswordEvent(String subject, String object) {
        super(subject, object);
    }
}
