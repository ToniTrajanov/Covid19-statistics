package mk.ukim.finki.covid19_statistics.model.exceptions;

public class LicenceNumberAlreadyExistsException extends RuntimeException {
    public LicenceNumberAlreadyExistsException() {
        super("Licence number is taken");
    }
}
