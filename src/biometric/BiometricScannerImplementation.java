package biometric;

import java.math.BigInteger;

public class BiometricScannerImplementation implements BiometricScanner {

    private BigInteger face, finger;

    public BiometricScannerImplementation(BigInteger face, BigInteger finger) {
        this.face = face;
        this.finger = finger;
    }

    @Override
    public BigInteger scanFace() {
        return face;
    }

    @Override
    public BigInteger scanFingerPrint() {
        return finger;
    }
}
