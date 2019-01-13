package verification;

import biometric.BiometricSoftware;
import data.Nif;
import exceptions.biometric.BiometricVerificationFailedException;
import exceptions.verification.VerificationIdentityFailedException;
import exceptions.data.NotValidNifException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiometricVerificationTest {

    private static class BiometricSoftwareCorrectStub implements BiometricSoftware {
        @Override
        public void verifyBiometricData() {

        }
    }

    private static class BiometricSoftwareIncorrectStub implements BiometricSoftware {
        @Override
        public void verifyBiometricData() throws BiometricVerificationFailedException {
            throw new BiometricVerificationFailedException();
        }
    }

    private static class IdentityVerifyMock implements verification.IdentityVerify {

        Nif nifToReturn;

        IdentityVerifyMock(Nif nif) {
            nifToReturn = nif;
        }

        @Override
        public Nif getNif() {
            return nifToReturn;
        }
    }

    private BiometricVerification biometricVerificationCorrectSoftware;
    private BiometricVerification biometricVerificationIncorrectSoftware;
    private BiometricVerification biometricVerificationIncorrectSoftwareNoAlternative;
    private Nif nif1, nif2;


    @BeforeEach
    void setUp() {
        nif1 = null;
        nif2 = null;
        try {
            nif1 = new Nif("12345678A");
            nif1 = new Nif("12345678B");
        } catch (NotValidNifException e) {
            e.printStackTrace();
            fail();
        }

        biometricVerificationCorrectSoftware = new BiometricVerification(
                nif1,
                new BiometricSoftwareCorrectStub(),
                new IdentityVerifyMock(nif2)
        );

        biometricVerificationIncorrectSoftware = new BiometricVerification(
                nif1,
                new BiometricSoftwareIncorrectStub(),
                new IdentityVerifyMock(nif2)
        );

        biometricVerificationIncorrectSoftwareNoAlternative = new BiometricVerification(
                nif1,
                new BiometricSoftwareIncorrectStub(),
                null
        );
    }

    @Test
    void CorrectSoftwareNotLaunchesAlternative() {
        try {
            assertEquals(nif1, biometricVerificationCorrectSoftware.getNif());
        } catch (VerificationIdentityFailedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void IncorrectSoftwareLaunchesAlternative() {
        try {
            assertEquals(nif2, biometricVerificationIncorrectSoftware.getNif());
        } catch (VerificationIdentityFailedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void IncorrectSoftwareNoAlternativeThrows() {
        assertThrows(VerificationIdentityFailedException.class, () -> biometricVerificationIncorrectSoftwareNoAlternative.getNif());
    }
}
