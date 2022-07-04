package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotDeleteVisitException extends RuntimeException {
    public CannotDeleteVisitException() {
        super("You are not allowed to delete this visit from here. Try deleting it from the referrals page.");
    }
}
