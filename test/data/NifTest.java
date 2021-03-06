package data;

import exceptions.data.NotValidNifException;
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
    void validNifTest() {
        Assertions.assertDoesNotThrow(() -> new Nif(dniCorrect));
        Assertions.assertDoesNotThrow(() -> new Nif(nieCorrect));

        Assertions.assertThrows(NotValidNifException.class, () -> new Nif(dniIncorrect));
        Assertions.assertThrows(NotValidNifException.class, () -> new Nif(nieIncorrect));
    }

    @Test
    @DisplayName("Check NIF getNif")
    void getNifTest() {
        try {
            Assertions.assertEquals(dniCorrect, new Nif(dniCorrect).getNif());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
