import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.*;
import exceptions.data.NotValidDigitalSignatureException;
import exceptions.data.NotValidMailException;
import exceptions.data.NotValidNifException;
import exceptions.data.NotValidPartyException;
import kiosk.VotingKiosk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ElectoralOrganism;
import services.MailerService;
import services.PartiesDB;
import verification.ManualVerification;
import verification.IdentityVerify;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VotingKioskTest {


    private static class ManualVerificationCorrectStub extends ManualVerification {
        @Override
        public boolean logInSupportStaff() {
            //First time login successfull
            return true;
        }

        @Override
        public Nif getManualNif() throws NotValidNifException {
            return new Nif("12345678A");
        }
    }

    /*
    private static class NoConnectivityToPartyDB implements PartiesDB {
        @Override
        public Set<Party> getPartiesFromDB() throws NoConnectionToDBException{
            throw new NoConnectionToDBException();
        }
    }

    private static class CorrectConnectToPartyDB implements PartiesDB {
        @Override
        public Set<Party> getPartiesFromDB() throws NoConnectionToDBException{
            return new HashSet<Party>() {{
                try {
                    add(new Party("PP"));
                    add(new Party("PSC"));
                    add(new Party("Cs"));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }};
        }
    }*/

    private static class TestElectoralOrganism implements ElectoralOrganism {
        boolean canVote = true;

        @Override
        public boolean canVote(Nif nif) {
            return canVote;
        }

        @Override
        public void disableVoter(Nif nif) {
            this.canVote = false;
        }

        @Override
        public DigitalSignature askForDigitalSignature(Party party) {
            try {
                return new DigitalSignature(cifrarOpcioVot(party));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private byte[] cifrarOpcioVot(Party party) {
            return new byte[32];
        }
    }

    private static class TestMailerService implements MailerService {
        public boolean emailSend = false;
        @Override
        public void send(MailAddress address, DigitalSignature signature) {
            this.emailSend = true;
        }
    }

    private VotingKiosk votingKiosk;
    private ElectoralOrganism teo;
    private TestMailerService mst;
    //private PartiesDB pdb;
    private IdentityVerify identityVerify;
    private MailAddress mailAddress;
    private Party party;

    @BeforeEach
    void setUp() {
        votingKiosk = new VotingKiosk();
        teo = new TestElectoralOrganism();
        mst = new TestMailerService();

        try {
            identityVerify = new ManualVerificationCorrectStub();
            mailAddress = new MailAddress("blabla@gmail.com");
            votingKiosk.setElectoralOrganism(teo);
            votingKiosk.setMailerService(mst);
            party = new Party("PP");
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("Start Session Test")
    void startSessionTest() {
        assertThrows(VerificationIdentityFailedException.class, () -> votingKiosk.startSession(null));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertThrows(SessionNotFinishedException.class, () -> votingKiosk.startSession(identityVerify));
    }

    @Test
    @DisplayName("Close Session Test")
    void closeSessionTest() {
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.closeSession());
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.closeSession());
    }

    /*
    @Test
    @DisplayName("Cannot connect to the DB Exception")
    void cantConnectDBTest() {
        pdb = new NoConnectivityToPartyDB();
        assertThrows(NoConnectionToDBException.class, () -> pdb.getPartiesFromDB());
    }
    */
    @Test
    void voteTest() {
        //pdb = new CorrectConnectToPartyDB();

        assertThrows(NullPointerException.class, () -> votingKiosk.vote(null));
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertThrows(VotingRightsFailedException.class, () -> votingKiosk.vote(party));
    }

    @Test
    void sendeReceiptTest() {
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertThrows(HasNotVotedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.sendeReceipt(mailAddress));
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void ElectoralOrganismTesting() {
        try {
            assertTrue(teo.canVote(new Nif("12345678A")), "Valid NIF can vote");
            teo.disableVoter(new Nif("12345678A"));
            assertFalse(teo.canVote(new Nif("12345678A")), "Same NIF as before, now cannot vote");

            assertEquals(new DigitalSignature(new byte[32]), teo.askForDigitalSignature(new Party("")));
        } catch (NotValidNifException | NotValidPartyException | NotValidDigitalSignatureException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void MailerServiceTest() {
        try {
            mst.send(new MailAddress("prova@gmail.com"), new DigitalSignature(new byte[32]));
            assertTrue(mst.emailSend, "Check if send");
        } catch (NotValidDigitalSignatureException | NotValidMailException e) {
            fail();
        }
    }
}
