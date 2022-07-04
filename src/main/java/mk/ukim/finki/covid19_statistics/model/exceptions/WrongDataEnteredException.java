package mk.ukim.finki.covid19_statistics.model.exceptions;

public class WrongDataEnteredException extends RuntimeException {
    public WrongDataEnteredException() {
        super("No doctor specified.");
    }
}
