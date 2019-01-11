package verification;

import biometric.BiometricSoftware;
import data.Nif;
import exceptions.BiometricVerificationFailedException;
import exceptions.VerificationIdentityFailedException;

public class BiometricVerification implements IdentityVerify {

    private Nif nifReadFromPassport;
    private BiometricSoftware software;
    private IdentityVerify alternative;

    public BiometricVerification(Nif nifReadFromPassport, BiometricSoftware software, IdentityVerify alternative) {
        if(nifReadFromPassport == null || software == null)
            throw new NullPointerException("Nif and software parameters musnt't be null");

        this.nifReadFromPassport = nifReadFromPassport;
        this.software = software;
        this.alternative = alternative;
    }

    @Override
    public Nif getNif() throws VerificationIdentityFailedException {
        try {
            software.verifyBiometricData();
            return nifReadFromPassport;
        } catch (BiometricVerificationFailedException e) {
            if (alternative == null)
                throw new VerificationIdentityFailedException(e.getMessage());
        }

        showMessageAlternativeStarted();
        return alternative.getNif();
    }


    private void showMessageAlternativeStarted() {
        System.out.println("Oops, your biometric data seems not to match. An alternative method is launched...");
    }
}
