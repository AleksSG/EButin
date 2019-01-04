package exceptions;

public class NotValidBiometricDataException extends Exception {
    public NotValidBiometricDataException(){
        super("Your biometric data seems to be incorrect.");
    }
}
