package exceptions;

public class NotValidBiometricFingerPrintException extends Exception {
    public NotValidBiometricFingerPrintException(){
        super("Your biometric fingerprint data seems to be incorrect, it must be 256-bit long (SHA1).");
    }
}
