package mk.ukim.finki.recruitment.model.exceptions;

public class EmailAlreadyAssociatedException extends RuntimeException {

    public EmailAlreadyAssociatedException(String email) {
        super(String.format("The email %s is already associated with an account.", email));
    }

}
