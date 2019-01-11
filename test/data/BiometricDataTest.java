package data;

import biometric.BiometricReader;
import biometric.BiometricScanner;
import biometric.BiometricSoftware;
import exceptions.BiometricVerificationFailedException;
import exceptions.data.NotValidBiometricDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BiometricDataTest {

    private static class BiometricScannerTest implements BiometricScanner {

        @Override
        public BigInteger scanFace() {
            return new BigInteger("12345");
        }

        @Override
        public BigInteger scanFingerPrint() {
            return new BigInteger("54321");
        }
    }

    private static class BiometricReaderTest implements BiometricReader {

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

    private static class BiometricSoftwareTest implements BiometricSoftware {

        private BiometricData passportData;
        private BiometricData scannedData;

        BiometricSoftwareTest(BiometricData passport, BiometricData scanned) {
            this.passportData = passport;
            this.scannedData = scanned;
        }

        @Override
        public void verifyBiometricData() throws BiometricVerificationFailedException {
            if (passportData.isNotSimilarTo(scannedData))
                throw new BiometricVerificationFailedException();
        }
    }

    private BiometricSoftware bsoft;
    private BiometricScanner bscan;
    private BiometricReader bread;

    @BeforeEach
    void setUp() {
        bscan = new BiometricScannerTest();
        bread = new BiometricReaderTest();
    }

    @Test
    @DisplayName("Scanning User")
    void scannerTest() {
        try {
            BiometricData dataScanned = new BiometricData(bscan.scanFace(), bscan.scanFingerPrint());
            assertEquals(new BiometricData(new BigInteger("12345"), new BigInteger("54321")), dataScanned);
            assertNotEquals(new BiometricData(new BigInteger("0"), new BigInteger("0")), dataScanned);
        } catch (NotValidBiometricDataException bfe) {
            bfe.printStackTrace();
            fail();
        }

    }

    @Test
    @DisplayName("Reading Password")
    void readPasswordTest() {
        try {
            BiometricData dataRead = bread.readBiometricData();

            assertEquals(new BiometricData(new BigInteger("12354"), new BigInteger("54312")), dataRead);
            assertNotEquals(new BiometricData(new BigInteger("0"), new BigInteger("0")), dataRead);
        } catch (NotValidBiometricDataException bfe) {
            bfe.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Verification test")
    void verificationTest() {
        try {
            this.bsoft = new BiometricSoftwareTest(new BiometricData(bscan.scanFace(), bscan.scanFingerPrint()), bread.readBiometricData());
            assertDoesNotThrow(() -> bsoft.verifyBiometricData());

            this.bsoft = new BiometricSoftwareTest(new BiometricData(bscan.scanFace(), bscan.scanFingerPrint()), new BiometricData(new BigInteger("0"), new BigInteger("0")));
            assertThrows(BiometricVerificationFailedException.class, () -> bsoft.verifyBiometricData());
        } catch (NotValidBiometricDataException bfe) {
            bfe.printStackTrace();
            fail();
        }
    }
}
