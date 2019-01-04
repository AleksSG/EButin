package biometric;

import exceptions.BiometricVerificationFailedException;

public interface BiometricSoftware {
    public void verifyBiometricData() throws BiometricVerificationFailedException;
}
