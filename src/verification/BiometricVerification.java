package verification;

import biometric.BiometricSoftware;
import data.Nif;
import exceptions.*;

public class BiometricVerification implements IdentityVerify {

    private BiometricSoftware software;
    private IdentityVerify alternative;

    public BiometricVerification(BiometricSoftware software, IdentityVerify alternative) {
        if(software == null)
            throw new NullPointerException("Parameter musnt't be null");

        this.software = software;
        this.alternative = alternative;
    }

    @Override
    public Nif getNif() throws VerificationIdentityFailedException {

        try {
            software.verifyBiometricData();

            return getNifFromPassport();

        } catch (BiometricVerificationFailedException e) {
            if(alternative == null)
                throw new VerificationIdentityFailedException(e.getMessage());
            showMessageAlternativeStarted();
            return alternative.getNif();
        } catch (NotValidNifException e) {
            if(alternative == null)
                throw new VerificationIdentityFailedException("Couldn't read the Nif from Passport");
            showMessageAlternativeStarted();
            return alternative.getNif();
        }
    }


    public void showMessageAlternativeStarted() {

    }

    public Nif getNifFromPassport() throws NotValidNifException {
        //TO-DO Implemment a read from Passport function
        throw new NotValidNifException("Nif wrong");
    }
}
