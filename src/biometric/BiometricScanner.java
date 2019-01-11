package biometric;

import java.math.BigInteger;

public interface BiometricScanner {
    BigInteger scanFace();
    BigInteger scanFingerPrint();
}
