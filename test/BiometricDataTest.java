import biometric.BiometricReader;
import biometric.BiometricScanner;
import biometric.BiometricSoftware;
import data.*;
import exceptions.BiometricVerificationFailedException;
import exceptions.NotValidBiometricDataException;
import exceptions.NotValidBiometricFacialException;
import exceptions.NotValidBiometricFingerPrintException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiometricDataTest {

    private static class BiometricScannerTest implements BiometricScanner {

        @Override
        public BiometricFacial scanFace() throws NotValidBiometricFacialException {
            return new BiometricFacial(new byte[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4});
        }

        @Override
        public BiometricFingerPrint scanFingerPrint() throws NotValidBiometricFingerPrintException {
            return new BiometricFingerPrint(new byte[]{10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13});
        }
    }

    private static class BiometricReaderTest implements BiometricReader {

        @Override
        public BiometricData readBiometricData() throws NotValidBiometricFingerPrintException, NotValidBiometricFacialException, NotValidBiometricDataException {
            return new BiometricData(readFacialFromPassport(), readFingerPrintFromPassport());
        }

        private BiometricFacial readFacialFromPassport() throws NotValidBiometricFacialException {
            return new BiometricFacial(new byte[32]);
        }

        private BiometricFingerPrint readFingerPrintFromPassport() throws NotValidBiometricFingerPrintException {
            return new BiometricFingerPrint(new byte[32]);
        }
    }

    private static class BiometricSoftwareTest implements BiometricSoftware {

        private BiometricData passportData;
        private BiometricData scannedData;

        public BiometricSoftwareTest(BiometricData passport, BiometricData scanned) {
            this.passportData = passport;
            this.scannedData = scanned;
        }

        @Override
        public void verifyBiometricData() throws BiometricVerificationFailedException {
            if (!this.passportData.equals(this.scannedData))
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
    @DisplayName("BiometricFacial is 32 bytes")
    void biometricFacial32bytes() {
        assertThrows(NotValidBiometricFacialException.class, () -> new BiometricFacial(new byte[]{1, 2, 3, 4}));
    }

    @Test
    @DisplayName("BiometricFingerPrint is 32 bytes")
    void biometricFingerPrint32bytes() {
        assertThrows(NotValidBiometricFingerPrintException.class, () -> new BiometricFingerPrint(new byte[]{1, 2, 3, 4}));
    }

    @Test
    @DisplayName("Scanning User")
    void scannerTest() {
        try {
            BiometricData dataScanned = new BiometricData(bscan.scanFace(), bscan.scanFingerPrint());

            assertEquals(dataScanned, new BiometricData(new BiometricFacial(new byte[]{1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4}),
                    new BiometricFingerPrint(new byte[]{10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13, 10, 11, 12, 13})
            ));

            assertNotEquals(dataScanned, new BiometricData(new BiometricFacial(new byte[32]), new BiometricFingerPrint(new byte[32])));
        } catch (NotValidBiometricFacialException | NotValidBiometricFingerPrintException | NotValidBiometricDataException bfe) {
            fail();
        }

    }

    
}
