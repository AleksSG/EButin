package exceptions;

public class NotValidBiometricFacialException extends Exception {
    public NotValidBiometricFacialException(){
        super("Your biometric facial data seems to be incorrect.");
    }
}
