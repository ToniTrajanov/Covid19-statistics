package mk.ukim.finki.covid19_statistics.model.exceptions;

public class DiagnosisNotFoundException extends RuntimeException{
    public DiagnosisNotFoundException() {
        super(String.format("Diagnosis not found!"));
    }
}
