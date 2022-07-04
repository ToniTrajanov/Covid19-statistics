package mk.ukim.finki.covid19_statistics.model.exceptions;

public class PatientDoesNotExistException extends RuntimeException {
    public PatientDoesNotExistException() {
        super(String.format("Patient with given data doesn't exist"));
    }
}
