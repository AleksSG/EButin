package biometric;

import data.BiometricFacial;
import data.BiometricFingerPrint;
import exceptions.NotValidBiometricFacialException;
import exceptions.NotValidBiometricFingerPrintException;

public interface BiometricScanner {
    public BiometricFacial scanFace() throws NotValidBiometricFacialException;
    public BiometricFingerPrint scanFingerPrint() throws NotValidBiometricFingerPrintException;
}
