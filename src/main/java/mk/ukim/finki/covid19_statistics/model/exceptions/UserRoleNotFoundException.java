package mk.ukim.finki.covid19_statistics.model.exceptions;

public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException() {
        super("No role has been chosen. Please select one");
    }
}
