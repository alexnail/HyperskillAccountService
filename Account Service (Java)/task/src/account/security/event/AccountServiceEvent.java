package account.security.event;


import lombok.Getter;

@Getter
public class AccountServiceEvent {
    protected final String subject;
    protected final String object;

    public AccountServiceEvent(String subject, String object) {
        this.subject = subject;
        this.object = object;
    }

    protected static String roleWoPrefix(String role) {
        return role.substring("ROLE_".length());
    }
}
