import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;
import exceptions.VerificationIdentityFailedException;
import exceptions.data.NotValidDigitalSignatureException;
import exceptions.data.NotValidNifException;
import kiosk.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import verification.IdentityVerify;

import static org.junit.jupiter.api.Assertions.fail;

class SessionTest {
    private static class WrongIdentityStub implements IdentityVerify {
        @Override
        public Nif getNif() throws VerificationIdentityFailedException {
            throw new VerificationIdentityFailedException("Something went wrong and the verification failed.");
        }
    }

    private static class RightIdentityStub implements IdentityVerify {
        @Override
        public Nif getNif() {
            Nif nif = null;
            try {
                nif = new Nif("12345678A");
            } catch (NotValidNifException e) {
                e.printStackTrace();
            }
            return nif;
        }
    }

    private IdentityVerify identityVerificationCorrect;
    private IdentityVerify identityVerificationIncorrect;
    private Session session;
    private DigitalSignature digitalSignature;

    @BeforeEach
    void setUp(){
        try {
            identityVerificationCorrect = new RightIdentityStub();
            identityVerificationIncorrect = new WrongIdentityStub();
            session = new Session(identityVerificationCorrect);
            digitalSignature = new DigitalSignature(new byte[32]);
        } catch (VerificationIdentityFailedException | NotValidDigitalSignatureException e) {
            fail();
        }
    }

    @Test
    void validSessionTest() {
        Assertions.assertDoesNotThrow(() -> new Session(identityVerificationCorrect));
        Assertions.assertThrows(VerificationIdentityFailedException.class, () -> new Session(null));
        Assertions.assertThrows(VerificationIdentityFailedException.class, () -> new Session(identityVerificationIncorrect));
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
