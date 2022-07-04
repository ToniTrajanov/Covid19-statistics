package mk.ukim.finki.covid19_statistics.model.exceptions;

public class UhidAlreadyExistsException extends RuntimeException {
    public UhidAlreadyExistsException() {
        super("Uhid already exists");
    }
}
