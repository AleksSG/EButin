import data.BiometricFingerPrint;
import exceptions.NotValidBiometricFingerPrintException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.fail;

class BiometricFingerPrintTest {
    private BigInteger bioFingerPrintCorrect;

    @BeforeEach
    void setUp(){
        bioFingerPrintCorrect = new BigInteger("123");
    }

    @Test
    @DisplayName("Check bioFingerPrint validation")
    void validBioFingerPrintTest() {
        Assertions.assertDoesNotThrow(() -> new BiometricFingerPrint(bioFingerPrintCorrect));
        Assertions.assertThrows(NotValidBiometricFingerPrintException.class, () -> new BiometricFingerPrint(null));
    }

    @Test
    @DisplayName("Check bioFingerPrint getBioFingerPrint")
    void getBioFingerPrintTest() {
        try {
            Assertions.assertEquals(bioFingerPrintCorrect, new BiometricFingerPrint(bioFingerPrintCorrect).getNumber());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
