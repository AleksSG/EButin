import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.*;
import kiosk.VotingKiosk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ElectoralOrganism;
import services.MailerService;
import services.PartiesDB;
import verification.IdentityVerify;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VotingKioskTest {

    private static class IdentityVerifyCorrectMock implements IdentityVerify {

        private Nif defaultNif;

        IdentityVerifyCorrectMock(Nif nif) {
            defaultNif = nif;
        }

        @Override
        public Nif getNif() {
            return defaultNif;
        }
    }

    private static class NoConnectivityToPartyDBStub implements PartiesDB {
        @Override
        public Set<Party> getPartiesFromDB() throws NoConnectionToDBException {
            throw new NoConnectionToDBException();
        }
    }

    private static class CorrectConnectToPartyDBStub implements PartiesDB {
        @Override
        public Set<Party> getPartiesFromDB() {
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
    }

    private static class ElectoralOrganismSpy implements ElectoralOrganism {

        boolean disableVoterCalled = false;
        boolean askedForSignature = false;

        @Override
        public boolean canVote(Nif nif) {
            return true;
        }

        @Override
        public void disableVoter(Nif nif) {
            this.disableVoterCalled = true;
        }

        @Override
        public DigitalSignature askForDigitalSignature(Party party) {
            this.askedForSignature = true;
            try {
                return new DigitalSignature(new byte[32]);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class MailerServiceSpy implements MailerService {

        boolean emailSend = false;

        @Override
        public void send(MailAddress address, DigitalSignature signature) {
            this.emailSend = true;
        }
    }

    private VotingKiosk votingKiosk;
    private ElectoralOrganismSpy teo;
    private MailerServiceSpy mst;
    private NoConnectivityToPartyDBStub incorrectPartyDB;
    private CorrectConnectToPartyDBStub correctPartyDB;
    private IdentityVerify identityVerify;
    private MailAddress mailAddress;
    private Party party;
    private Nif nif;

    @BeforeEach
    void setUp() {
        votingKiosk = new VotingKiosk();
        incorrectPartyDB = new NoConnectivityToPartyDBStub();
        correctPartyDB = new CorrectConnectToPartyDBStub();
        teo = new ElectoralOrganismSpy();
        mst = new MailerServiceSpy();

        try {
            nif = new Nif("12345678A");
            identityVerify = new IdentityVerifyCorrectMock(nif);
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

    @Test
    void voteTest() {
        assertThrows(NoConnectionToDBException.class, () -> votingKiosk.setPartiesDB(incorrectPartyDB));
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertThrows(NullPointerException.class, () -> votingKiosk.vote(null));
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertTrue(teo.disableVoterCalled);
        assertTrue(teo.askedForSignature);
    }

    @Test
    void sendeReceiptTest() {
        assertThrows(NoConnectionToDBException.class, () -> votingKiosk.setPartiesDB(incorrectPartyDB));
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertThrows(HasNotVotedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.sendeReceipt(mailAddress));
        assertTrue(mst.emailSend);
    }
}
