package biometric;

import data.BiometricData;
import exceptions.NotValidBiometricDataException;
import exceptions.NotValidBiometricFacialException;
import exceptions.NotValidBiometricFingerPrintException;

public interface BiometricReader {
    public BiometricData readBiometricData() throws NotValidBiometricFingerPrintException, NotValidBiometricFacialException, NotValidBiometricDataException;
}
