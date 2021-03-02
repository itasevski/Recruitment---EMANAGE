package mk.ukim.finki.recruitment.model.exceptions;

public class AdAlreadySavedException extends RuntimeException {

    public AdAlreadySavedException(Long id) {
        super(String.format("Ad with id %d is already saved.", id));
    }

}
