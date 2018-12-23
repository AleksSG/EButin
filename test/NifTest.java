import data.Nif;
import exceptions.InvalidNifException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;

class NifTest {
    private String dniCorrect;
    private String dniIncorrect;
    private String nieCorrect;
    private String nieIncorrect;

    @BeforeEach
    void setUp(){
        dniCorrect = "11223344A";
        dniIncorrect = "11223344I";
        nieCorrect = "X1122344A";
        nieIncorrect = "O11223344A";
    }

    @Test
    @DisplayName("Check NIF validation")
    void validNif() {
        Assertions.assertDoesNotThrow(() -> new Nif(dniCorrect));
        Assertions.assertDoesNotThrow(() -> new Nif(nieCorrect));

        Assertions.assertThrows(InvalidNifException.class, () -> new Nif(dniIncorrect));
        Assertions.assertThrows(InvalidNifException.class, () -> new Nif(nieIncorrect));
    }

    @Test
    @DisplayName("Check NIF getNif")
    void getNif() {
        try {
            Assertions.assertEquals(dniCorrect, new Nif(dniCorrect).getNif());
            Assertions.assertTrue(true);
        }
        catch(Exception e) {
            fail();
        }
    }
}
