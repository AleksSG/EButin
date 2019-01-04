package biometric;

import exceptions.BiometricVerificationFailedException;

public interface BiometricSoftware {
    void verifyBiometricData() throws BiometricVerificationFailedException;
}
