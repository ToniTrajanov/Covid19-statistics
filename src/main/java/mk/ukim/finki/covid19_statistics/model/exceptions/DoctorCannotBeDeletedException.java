package mk.ukim.finki.covid19_statistics.model.exceptions;

public class DoctorCannotBeDeletedException extends RuntimeException {
    public DoctorCannotBeDeletedException() {
        super("You are not allowed to delete this doctor");
    }
}
