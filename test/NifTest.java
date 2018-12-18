import data.Nif;
import org.junit.jupiter.api.*;

class NifTest {
    private Nif dniCorrect;
    private Nif dniIncorrect;
    private Nif nieCorrect;
    private Nif nieIncorrect;

    @BeforeEach
    void setUp(){
        dniCorrect = new Nif("11223344A");
        dniIncorrect = new Nif("11223344I");
        nieCorrect = new Nif("X1122344A");
        nieIncorrect = new Nif("O11223344A");
    }

    @Test
    @DisplayName("Check NIF")
    void validNif() {
        Assertions.assertTrue(dniCorrect.isValid());
        Assertions.assertFalse(dniIncorrect.isValid());
        Assertions.assertTrue(nieCorrect.isValid());
        Assertions.assertFalse(nieIncorrect.isValid());
    }
}
