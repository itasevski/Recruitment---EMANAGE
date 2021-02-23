package mk.ukim.finki.recruitment.model.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String uuid) {
        super(String.format("%s: Account with given UUID doesn't exist.", uuid));
    }

}
