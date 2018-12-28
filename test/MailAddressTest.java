import data.MailAddress;
import data.Nif;
import exceptions.NotValidMailException;
import exceptions.NotValidNifException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class MailAddressTest {
    private String mailAddressCorrect;
    private String mailAddressIncorrect;

    @BeforeEach
    void setUp(){
        mailAddressCorrect = "adreça@gmail.com";
        mailAddressIncorrect = "correu@@fals.";
    }

    @Test
    @DisplayName("Check MailAddress validation")
    void validMailAddressTest() {
        Assertions.assertDoesNotThrow(() -> new MailAddress(mailAddressCorrect));
        Assertions.assertThrows(NotValidMailException.class, () -> new MailAddress(null));
        Assertions.assertThrows(NotValidMailException.class, () -> new MailAddress(mailAddressIncorrect));
    }

    @Test
    @DisplayName("Check MailAdress getMail")
    void getMailTest() {
        try {
            Assertions.assertEquals(mailAddressCorrect, new MailAddress(mailAddressCorrect).getMail());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
