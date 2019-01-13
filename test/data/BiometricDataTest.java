package data;

import exceptions.data.NotValidBiometricDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class BiometricDataTest {

    private BigInteger faceSimilar, fingerSimilar, faceNotSimilar, fingerNotSimilar;
    private BiometricData similar, notSimilar;

    @BeforeEach
    void setUp() {
        faceSimilar = new BigInteger("12345");
        fingerSimilar = new BigInteger("54321");
        faceNotSimilar = new BigInteger("92345");
        fingerNotSimilar = new BigInteger("94321");

        try {
            similar = new BiometricData(faceSimilar, fingerSimilar);
            notSimilar = new BiometricData(faceNotSimilar, fingerNotSimilar);
        } catch (NotValidBiometricDataException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void exceptionIsThrownWhenParametersAreNull() {
        assertThrows(NotValidBiometricDataException.class, () -> new BiometricData(null, null));
    }

    @Test
    void exceptionIsThrownWhenAParameterIsNull() {
        assertThrows(NotValidBiometricDataException.class, () -> new BiometricData(faceNotSimilar, null));
        assertThrows(NotValidBiometricDataException.class, () -> new BiometricData(null, fingerNotSimilar));
    }

    @Test
    void noExceptionIsThrownWhenParametersAreNotNull() {
        assertDoesNotThrow(() -> new BiometricData(faceNotSimilar, fingerNotSimilar));
    }

    @Test
    void checkSimilarNumbers() {
        assertFalse(similar.isNotSimilarTo(similar));
        assertTrue(similar.isNotSimilarTo(notSimilar));
    }
}
