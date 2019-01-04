package exceptions;

public class NotValidBiometricFacialException extends Exception {
    public NotValidBiometricFacialException(){
        super("Your biometric facial data seems to be incorrect, it must be 256-bit long (SHA1).");
    }
}
