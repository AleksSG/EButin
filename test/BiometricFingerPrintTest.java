import data.BiometricFingerPrint;
import exceptions.NotValidBiometricFingerPrintException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class BiometricFingerPrintTest {
    private byte[] bioFingerPrintCorrect, bioFingerPrintIncorrect;

    @BeforeEach
    void setUp(){
        bioFingerPrintCorrect = new byte[32];
        bioFingerPrintIncorrect = new byte[33];
    }

    @Test
    @DisplayName("Check bioFingerPrint validation")
    void validBioFingerPrintTest() {
        Assertions.assertDoesNotThrow(() -> new BiometricFingerPrint(bioFingerPrintCorrect));
        Assertions.assertThrows(NotValidBiometricFingerPrintException.class, () -> new BiometricFingerPrint(null));
        Assertions.assertThrows(NotValidBiometricFingerPrintException.class, () -> new BiometricFingerPrint(bioFingerPrintIncorrect));
    }

    @Test
    @DisplayName("Check bioFingerPrint getBioFingerPrint")
    void getBioFingerPrintTest() {
        try {
            Assertions.assertEquals(bioFingerPrintCorrect, new BiometricFingerPrint(bioFingerPrintCorrect).getBioFingerPrint());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
