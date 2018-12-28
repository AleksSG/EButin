package exceptions;

public class NotValidDigitalSignatureException extends Exception {
    public NotValidDigitalSignatureException(){
        super("Your digital signature seems to be incorrect.");
    }
}
