package mk.ukim.finki.covid19_statistics.model.exceptions;

public class DoctorNotSelectedException extends RuntimeException {
    public DoctorNotSelectedException() {
        super("You didn't select a doctor.");
    }
}
