package account.exception;

public class PasswordLengthException extends RuntimeException {

    public PasswordLengthException() {
        super("Password length must be 12 chars minimum!");
    }
}
