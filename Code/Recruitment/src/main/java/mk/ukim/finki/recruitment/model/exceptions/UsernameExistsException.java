package mk.ukim.finki.recruitment.model.exceptions;

public class UsernameExistsException extends RuntimeException {

    public UsernameExistsException(String username) {
        super(String.format("The username %s already exists.", username));
    }

}
