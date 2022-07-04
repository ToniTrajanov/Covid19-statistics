package mk.ukim.finki.covid19_statistics.model.exceptions;

public class SpecialtyAlreadyExistsException extends RuntimeException {
        public SpecialtyAlreadyExistsException(String message) {
            super(String.format("Specialty with name %s already exists", message));
        }
}
