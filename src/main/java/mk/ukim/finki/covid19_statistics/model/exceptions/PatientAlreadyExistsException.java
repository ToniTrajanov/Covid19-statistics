package mk.ukim.finki.covid19_statistics.model.exceptions;

public class PatientAlreadyExistsException extends RuntimeException {
    public PatientAlreadyExistsException() {
        super("Patient already exists");
    }
}
