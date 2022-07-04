package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotDeleteSpecialtyException extends RuntimeException {
    public CannotDeleteSpecialtyException() {
        super("You are not allowed to delete specialty");
    }
}
