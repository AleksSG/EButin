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

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static utils.BigMaths.areBigIntegersSimilar;

class BiometricDataTest {

    private static class BiometricScannerTest implements BiometricScanner {

        @Override
        public BiometricFacial scanFace() throws NotValidBiometricFacialException {
            return new BiometricFacial(new BigInteger("12345"));
        }

        @Override
        public BiometricFingerPrint scanFingerPrint() throws NotValidBiometricFingerPrintException {
            return new BiometricFingerPrint(new BigInteger("54321"));
        }
    }

    private static class BiometricReaderTest implements BiometricReader {

        @Override
        public BiometricData readBiometricData() throws NotValidBiometricFingerPrintException, NotValidBiometricFacialException, NotValidBiometricDataException {
            return new BiometricData(readFacialFromPassport(), readFingerPrintFromPassport());
        }

        private BiometricFacial readFacialFromPassport() throws NotValidBiometricFacialException {
            return new BiometricFacial(new BigInteger("12354"));
        }

        private BiometricFingerPrint readFingerPrintFromPassport() throws NotValidBiometricFingerPrintException {
            return new BiometricFingerPrint(new BigInteger("54312"));
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
            if (!areBigIntegersSimilar(passportData.getBioFacial().getNumber(), scannedData.getBioFacial().getNumber()) || !areBigIntegersSimilar(passportData.getBioFingerPrint().getNumber(), scannedData.getBioFingerPrint().getNumber()))
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

            assertEquals(new BiometricData(new BiometricFacial(new BigInteger("12345")), new BiometricFingerPrint(new BigInteger("54321"))), dataScanned);
            assertNotEquals(new BiometricData(new BiometricFacial(new BigInteger("0")), new BiometricFingerPrint(new BigInteger("0"))), dataScanned);
        } catch (NotValidBiometricFacialException | NotValidBiometricFingerPrintException | NotValidBiometricDataException bfe) {
            fail();
        }

    }

    @Test
    @DisplayName("Reading Password")
    void readPasswordTest() {
        try {
            BiometricData dataRead = bread.readBiometricData();

            assertEquals(new BiometricData(new BiometricFacial(new BigInteger("12354")), new BiometricFingerPrint(new BigInteger("54312"))), dataRead);
            assertNotEquals(new BiometricData(new BiometricFacial(new BigInteger("0")), new BiometricFingerPrint(new BigInteger("0"))), dataRead);
        } catch (NotValidBiometricFacialException | NotValidBiometricFingerPrintException | NotValidBiometricDataException bfe) {
            fail();
        }
    }

    @Test
    @DisplayName("Verification test")
    void verificationTest() {
        try {
            this.bsoft = new BiometricSoftwareTest(new BiometricData(bscan.scanFace(), bscan.scanFingerPrint()), bread.readBiometricData());
            assertDoesNotThrow(() -> bsoft.verifyBiometricData());

            this.bsoft = new BiometricSoftwareTest(new BiometricData(bscan.scanFace(), bscan.scanFingerPrint()), new BiometricData(new BiometricFacial(new BigInteger("0")), new BiometricFingerPrint(new BigInteger("0"))));
            assertThrows(BiometricVerificationFailedException.class, () -> bsoft.verifyBiometricData());
        } catch (NotValidBiometricFacialException | NotValidBiometricFingerPrintException | NotValidBiometricDataException bfe) {
            fail();
        }
    }

    
}
