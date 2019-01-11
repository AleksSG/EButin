package biometric;

import data.BiometricData;
import exceptions.data.NotValidBiometricDataException;

public interface BiometricReader {
    BiometricData readBiometricData() throws NotValidBiometricDataException;
}
