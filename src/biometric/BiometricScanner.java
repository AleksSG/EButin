package biometric;

import data.BiometricFacial;
import data.BiometricFingerPrint;

public interface BiometricScanner {
    public BiometricFacial scanFace();
    public BiometricFingerPrint[] scanFingerPrint();
}
