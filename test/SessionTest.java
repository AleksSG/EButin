import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;
import exceptions.NotValidDigitalSignatureException;
import exceptions.NotValidNifException;
import exceptions.VerificationIdentityFailedException;
import kiosk.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import verification.ManualVerification;
import verification.IdentityVerify;

import static org.junit.jupiter.api.Assertions.fail;

class SessionTest {
    private static class ManualVerificationCorrectDouble extends ManualVerification {

        @Override
        public boolean logInSupportStaff() {
            //First time login successfull
            return true;
        }

        @Override
        public Nif getManualNif() throws NotValidNifException {
            return new Nif("12345678Z");
        }
    }

    private static class ManualVerificationIncorrectDouble extends ManualVerification {

        @Override
        public boolean logInSupportStaff() {
            //The staff member never logging in succesfully...
            return false;
        }

        @Override
        public Nif getManualNif() {
            return null;
        }
    }

    private IdentityVerify identityVerificationCorrect;
    private IdentityVerify identityVerificationIncorrect;
    private Session session;
    private DigitalSignature digitalSignature;

    @BeforeEach
    void setUp(){
        try {
            identityVerificationCorrect = new ManualVerificationCorrectDouble();
            identityVerificationIncorrect = new ManualVerificationIncorrectDouble();
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
