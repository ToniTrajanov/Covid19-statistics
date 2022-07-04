package mk.ukim.finki.covid19_statistics.model.exceptions;

public class ReceiptNotFoundException extends RuntimeException{
    public ReceiptNotFoundException() {
        super("Receipt not found exception");
    }
}
