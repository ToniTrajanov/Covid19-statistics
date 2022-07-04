package mk.ukim.finki.covid19_statistics.model.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TermIsNotAvailableException extends RuntimeException {
    public TermIsNotAvailableException(String term) {
        super(String.format("The term %s is not available", term));
    }
}
