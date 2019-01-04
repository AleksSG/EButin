import data.BiometricFacial;
import data.DigitalSignature;
import exceptions.NotValidBiometricFacialException;
import exceptions.NotValidDigitalSignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class BiometricFacialTest {
    private byte[] bioFacialCorrect, bioFacialIncorrect;

    @BeforeEach
    void setUp(){
        bioFacialCorrect = new byte[32];
        bioFacialIncorrect = new byte[33];
    }

    @Test
    @DisplayName("Check biometricFacial validation")
    void validBiometricFacialTest() {
        Assertions.assertDoesNotThrow(() -> new BiometricFacial(bioFacialCorrect));
        Assertions.assertThrows(NotValidBiometricFacialException.class, () -> new BiometricFacial(null));
        Assertions.assertThrows(NotValidBiometricFacialException.class, () -> new BiometricFacial(bioFacialIncorrect));
    }

    @Test
    @DisplayName("Check biometricFacial getBioFacial")
    void getBioFacialTest() {
        try {
            Assertions.assertEquals(bioFacialCorrect, new BiometricFacial(bioFacialCorrect).getBioFacial());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
