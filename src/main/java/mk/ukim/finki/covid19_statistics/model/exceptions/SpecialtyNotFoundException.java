package mk.ukim.finki.covid19_statistics.model.exceptions;

public class SpecialtyNotFoundException extends RuntimeException {
    public SpecialtyNotFoundException() {
        super("Specialty not found");
    }
}
