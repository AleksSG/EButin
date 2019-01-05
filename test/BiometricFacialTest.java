import data.BiometricFacial;
import data.DigitalSignature;
import exceptions.NotValidBiometricFacialException;
import exceptions.NotValidDigitalSignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.fail;

class BiometricFacialTest {
    private BigInteger bioFacialCorrect;

    @BeforeEach
    void setUp(){
        bioFacialCorrect = new BigInteger("123");
    }

    @Test
    @DisplayName("Check biometricFacial validation")
    void validBiometricFacialTest() {
        Assertions.assertDoesNotThrow(() -> new BiometricFacial(bioFacialCorrect));
        Assertions.assertThrows(NotValidBiometricFacialException.class, () -> new BiometricFacial(null));
    }

    @Test
    @DisplayName("Check biometricFacial getBioFacial")
    void getBioFacialTest() {
        try {
            Assertions.assertEquals(bioFacialCorrect, new BiometricFacial(bioFacialCorrect).getNumber());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
