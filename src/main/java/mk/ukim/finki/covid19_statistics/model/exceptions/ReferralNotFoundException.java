package mk.ukim.finki.covid19_statistics.model.exceptions;

public class ReferralNotFoundException extends RuntimeException{
    public ReferralNotFoundException(Long id) {
        super(String.format("Referral with id %d was not found", id));
    }
}
