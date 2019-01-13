package biometric;

import data.BiometricData;
import exceptions.biometric.BiometricVerificationFailedException;
import exceptions.data.NotValidBiometricDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BiometricSoftwareTest {

    private static class BiometricReaderSpy implements BiometricReader {
        boolean isReadBiometricDataCalled = false;

        @Override
        public BiometricData readBiometricData() throws NotValidBiometricDataException {
            isReadBiometricDataCalled = true;
            return new BiometricData(readFacialFromPassport(), readFingerPrintFromPassport());
        }

        private BigInteger readFacialFromPassport() {
            return new BigInteger("12354");
        }

        private BigInteger readFingerPrintFromPassport() {
            return new BigInteger("54312");
        }
    }

    private static class BiometricScannerSpy implements BiometricScanner {
        boolean isScanFaceCalled = false, isScanFingerPrintCalled = false;

        @Override
        public BigInteger scanFace() {
            isScanFaceCalled = true;
            return new BigInteger("12345");
        }

        @Override
        public BigInteger scanFingerPrint() {
            isScanFingerPrintCalled = true;
            return new BigInteger("54321");
        }
    }

    private static class BiometricScannerSpyNotSimilarData implements BiometricScanner {
        boolean isScanFaceCalled = false, isScanFingerPrintCalled = false;

        @Override
        public BigInteger scanFace() {
            isScanFaceCalled = true;
            return new BigInteger("42345");
        }

        @Override
        public BigInteger scanFingerPrint() {
            isScanFingerPrintCalled = true;
            return new BigInteger("54321");
        }
    }

    private BiometricReaderSpy biometricReader;
    private BiometricScannerSpy biometricScanner;
    private BiometricScannerSpyNotSimilarData biometricScannerNotSimilar;
    private BiometricSoftwareImplementation biometricSoftwareImplementation, biometricSoftwareImplementationNotSimilar;

    @BeforeEach
    void setUp() {
        biometricReader = new BiometricReaderSpy();
        biometricScanner = new BiometricScannerSpy();
        biometricScannerNotSimilar = new BiometricScannerSpyNotSimilarData();
        biometricSoftwareImplementation = new BiometricSoftwareImplementation(biometricReader, biometricScanner);
        biometricSoftwareImplementationNotSimilar = new BiometricSoftwareImplementation(biometricReader, biometricScannerNotSimilar);
    }

    @Test
    void softwareCallsBiometricReaderAndScanner() {
        assertDoesNotThrow(() -> biometricSoftwareImplementation.verifyBiometricData());
        assertTrue(biometricReader.isReadBiometricDataCalled);
        assertTrue(biometricScanner.isScanFaceCalled);
        assertTrue(biometricScanner.isScanFingerPrintCalled);
    }

    @Test
    void softwareThrowsWhenDataNotSimilar() {
        assertThrows(BiometricVerificationFailedException.class,() -> biometricSoftwareImplementationNotSimilar.verifyBiometricData());
        assertTrue(biometricReader.isReadBiometricDataCalled);
        assertTrue(biometricScannerNotSimilar.isScanFaceCalled);
        assertTrue(biometricScannerNotSimilar.isScanFingerPrintCalled);
    }
}
