package exceptions;

public class NotValidBiometricFingerPrintException extends Exception {
    public NotValidBiometricFingerPrintException(){
        super("Your biometric fingerprint data seems to be incorrect.");
    }
}
