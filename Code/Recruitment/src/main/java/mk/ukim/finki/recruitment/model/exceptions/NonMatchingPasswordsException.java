package mk.ukim.finki.recruitment.model.exceptions;

public class NonMatchingPasswordsException extends RuntimeException {

    public NonMatchingPasswordsException() {
        super("Invalid. Passwords don't match.");
    }

}
