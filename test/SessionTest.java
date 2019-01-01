import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;
import exceptions.NotValidDigitalSignatureException;
import exceptions.NotValidNifException;
import kiosk.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class SessionTest {
    private Nif nif;
    private Session session;
    private DigitalSignature digitalSignature;

    @BeforeEach
    void setUp(){
        try {
            nif = new Nif("12345678Z");
            session = new Session(nif);
            digitalSignature = new DigitalSignature(new byte[] {1, 2, 3, 4});
        } catch (NotValidNifException | NotValidDigitalSignatureException e) {
            fail();
        }
    }

    @Test
    void validSessionTest() {
        Assertions.assertDoesNotThrow(() -> new Session(nif));
        Assertions.assertThrows(NotValidNifException.class, () -> new Session(null));
    }

    @Test
    void digitalSignatureSessionTest() {
        Assertions.assertThrows(NotValidDigitalSignatureException.class, () -> session.setDigitalSignature(null));
        Assertions.assertThrows(HasNotVotedException.class, () -> session.getDigitalSignature());
        Assertions.assertDoesNotThrow(() -> session.setDigitalSignature(digitalSignature));
        try {
            Assertions.assertEquals(digitalSignature, session.getDigitalSignature());
        }
        catch (Exception e) {
            fail();
        }
    }
}
