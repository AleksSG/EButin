package exceptions;

public class BiometricVerificationFailedException extends Exception{
    public BiometricVerificationFailedException(){
        super("Passport does not match with Biometric Data Scan");
    }
}
