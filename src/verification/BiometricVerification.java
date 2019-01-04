package verification;

import biometric.BiometricSoftware;
import data.Nif;
import exceptions.*;

public class BiometricVerification implements IdentityVerify {

    private BiometricSoftware software;

    public BiometricVerification(BiometricSoftware software) {
        if(software == null)
            throw new NullPointerException("Parameter musnt't be null");

        this.software = software;
    }

    @Override
    public Nif getNif() throws VerificationIdentityFailedException {

        try {
            software.verifyBiometricData();

            return getNifFromPassport();

        } catch (BiometricVerificationFailedException e) {
            throw new VerificationIdentityFailedException(e.getMessage());
        } catch (NotValidNifException e) {
            throw new VerificationIdentityFailedException("Couldn't read the Nif from Passport");
        }
    }

    public Nif getNifFromPassport() throws NotValidNifException {
        //TO-DO Implemment a read from Passport function
        throw new NotValidNifException("Nif wrong");
    }
}
