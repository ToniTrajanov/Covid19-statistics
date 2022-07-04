package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotDeleteDiagnosisException extends RuntimeException {
    public CannotDeleteDiagnosisException() {
        super("You are not allowed to delete this diagnosis");
    }
}
