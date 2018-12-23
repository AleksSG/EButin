import data.Nif;
import org.junit.jupiter.api.*;

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
    @DisplayName("Check NIF")
    void validNif() {
        Assertions.assertTrue(dniCorrect.isValid());
        Assertions.assertFalse(dniIncorrect.isValid());
        Assertions.assertTrue(nieCorrect.isValid());
        Assertions.assertFalse(nieIncorrect.isValid());
    }
}
