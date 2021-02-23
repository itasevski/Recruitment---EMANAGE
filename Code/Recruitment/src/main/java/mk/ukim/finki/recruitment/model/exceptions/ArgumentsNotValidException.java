package mk.ukim.finki.recruitment.model.exceptions;

public class ArgumentsNotValidException extends RuntimeException {

    public ArgumentsNotValidException() {
        super("Invalid arguments. Make sure you insert correct information.");
    }

}
