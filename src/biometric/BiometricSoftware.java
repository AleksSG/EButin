package biometric;

import exceptions.biometric.BiometricVerificationFailedException;

public interface BiometricSoftware {
    void verifyBiometricData() throws BiometricVerificationFailedException;
}
