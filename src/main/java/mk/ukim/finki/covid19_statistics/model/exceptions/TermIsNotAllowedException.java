package mk.ukim.finki.covid19_statistics.model.exceptions;


public class TermIsNotAllowedException extends RuntimeException {
    public TermIsNotAllowedException() {
        super("You cannot select a date before today's date.");
    }
}
