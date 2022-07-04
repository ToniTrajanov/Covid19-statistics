package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotDeleteVisitWithDiagnosisException extends RuntimeException {
    public CannotDeleteVisitWithDiagnosisException() {
        super("You are not allowed to delete this visit because it has a diagnose attached to it.");
    }
}
