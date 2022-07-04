package mk.ukim.finki.covid19_statistics.model.exceptions;

public class InvalidArgumentException extends RuntimeException{
    public InvalidArgumentException() {
        super("Invalid arguments. Please fill every field with correct data.");
    }
}