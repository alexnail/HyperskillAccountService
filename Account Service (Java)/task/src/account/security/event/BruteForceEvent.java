package account.security.event;

public class BruteForceEvent extends AccountServiceEvent {

    public BruteForceEvent(String subject, String object) {
        super(subject, object);
    }
}
