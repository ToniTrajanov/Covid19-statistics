package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotEditVisitException extends RuntimeException {
    public CannotEditVisitException() {
        super("You are not allowed to edit this visit. Do it from the referrals page");
    }
}
