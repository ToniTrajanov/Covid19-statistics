package mk.ukim.finki.covid19_statistics.model.exceptions;

public class NothingFoundException extends RuntimeException{
    public NothingFoundException() {
        super("Nothing matched your search");
    }
}
