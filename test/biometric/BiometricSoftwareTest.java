package biometric;

import data.BiometricData;
import exceptions.data.NotValidBiometricDataException;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class BiometricSoftwareTest {
    private static class BiometricScannerStub implements BiometricScanner {
        @Override
        public BigInteger scanFace() {
            return new BigInteger("12345");
        }

        @Override
        public BigInteger scanFingerPrint() {
            return new BigInteger("54321");
        }
    }
    private static class BiometricReaderStub implements BiometricReader {

        @Override
        public BiometricData readBiometricData() throws NotValidBiometricDataException {
            return new BiometricData(readFacialFromPassport(), readFingerPrintFromPassport());
        }

        private BigInteger readFacialFromPassport() {
            return new BigInteger("12354");
        }

        private BigInteger readFingerPrintFromPassport() {
            return new BigInteger("54312");
        }
    }


}
