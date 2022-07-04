package mk.ukim.finki.covid19_statistics.model.exceptions;

public class VisitNotFoundException extends RuntimeException {
    public VisitNotFoundException() {
        super(String.format("Visit not found!"));
    }
}
