package mk.ukim.finki.covid19_statistics.model.exceptions;

public class InvalidUserCredentialException extends RuntimeException{
    public InvalidUserCredentialException() {
        super("Invalid user credentials!");
    }
}