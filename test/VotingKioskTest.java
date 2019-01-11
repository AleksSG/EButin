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
import verification.ManualVerification;
import verification.IdentityVerify;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VotingKioskTest {

    private static class ManualVerificationCorrectDouble extends ManualVerification {

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

    private static class VotingKioskDouble extends VotingKiosk {
        @Override
        public Set<Party> getPartiesFromDB() {
            Set<Party> partySet = new HashSet<>();

            try {
                partySet.add(new Party("PP"));
                partySet.add(new Party("PSC"));
                partySet.add(new Party("Cs"));
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return partySet;
        }
    }

    private static class TestElectoralOrganism implements ElectoralOrganism {

        private HashSet<Nif> canVoteNif;

        TestElectoralOrganism() {
            try {
                createSet();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean canVote(Nif nif) {
            return canVoteNif.contains(nif);
        }

        @Override
        public void disableVoter(Nif nif) {
            canVoteNif.remove(nif);
        }

        private void createSet() throws NotValidNifException {
            canVoteNif = new HashSet<>();
            canVoteNif.add(new Nif("12345678A"));
            canVoteNif.add(new Nif("A1234567A"));
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
        private boolean emailSend = false;
        @Override
        public void send(MailAddress address, DigitalSignature signature) {
            this.emailSend = true;
        }

        boolean isSend(){
            return this.emailSend;
        }
    }

    private VotingKiosk votingKiosk;
    private ElectoralOrganism teo;
    private TestMailerService mst;
    private IdentityVerify identityVerify;
    private MailAddress mailAddress;
    private Party party;

    @BeforeEach
    void setUp() {
        votingKiosk = new VotingKioskDouble();
        teo = new TestElectoralOrganism();
        mst = new TestMailerService();

        try {
            identityVerify = new ManualVerificationCorrectDouble();
            mailAddress = new MailAddress("blabla@gmail.com");
            party = new Party("PP");
            votingKiosk.setElectoralOrganism(teo);
            votingKiosk.setMailerService(mst);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DisplayName("startSessionTest")
    void startSessionTest() {
        assertThrows(VerificationIdentityFailedException.class, () -> votingKiosk.startSession(null));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertThrows(SessionNotFinishedException.class, () -> votingKiosk.startSession(identityVerify));
    }

    @Test
    @DisplayName("closeSessionTest")
    void closeSessionTest() {
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.closeSession());
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.closeSession());
    }

    @Test
    void voteTest() {
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
            assertFalse(teo.canVote(new Nif("23456789A")), "Valid NIF cannot vote");

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
            assertTrue(mst.isSend(), "Check if send");
        } catch (NotValidDigitalSignatureException | NotValidMailException e) {
            fail();
        }
    }
}
