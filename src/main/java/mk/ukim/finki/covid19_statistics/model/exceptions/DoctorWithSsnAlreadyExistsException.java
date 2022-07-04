package mk.ukim.finki.covid19_statistics.model.exceptions;

public class DoctorWithSsnAlreadyExistsException extends RuntimeException {
    public DoctorWithSsnAlreadyExistsException() {
        super("Doctor with given SSN already exists");
    }
}
