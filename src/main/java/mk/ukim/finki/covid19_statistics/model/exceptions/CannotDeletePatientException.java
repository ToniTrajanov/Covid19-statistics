package mk.ukim.finki.covid19_statistics.model.exceptions;

public class CannotDeletePatientException extends RuntimeException{
    public CannotDeletePatientException() {
        super("You are not allowed to delete this patient");
    }
}
