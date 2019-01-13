package kiosk;

import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.data.NotValidPartyException;
import exceptions.kiosk.*;
import exceptions.services.NoConnectionToDBException;
import exceptions.verification.VerificationIdentityFailedException;
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
        boolean askedForCanVote = false;

        @Override
        public boolean canVote(Nif nif) {
            askedForCanVote = true;
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

    private static class ElectoralOrganismSpyCanNotVote implements ElectoralOrganism {

        boolean disableVoterCalled = false;
        boolean askedForSignature = false;
        boolean askedForCanVote = false;

        @Override
        public boolean canVote(Nif nif) {
            askedForCanVote = true;
            return false;
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

    private VotingKiosk votingKiosk, votingKioskCanNotVote;
    private ElectoralOrganismSpy teo;
    private ElectoralOrganismSpyCanNotVote teoIncorrect;
    private MailerServiceSpy mst;
    private NoConnectivityToPartyDBStub incorrectPartyDB;
    private CorrectConnectToPartyDBStub correctPartyDB;
    private IdentityVerify identityVerify;
    private MailAddress mailAddress;
    private Party party;

    @BeforeEach
    void setUp() {
        votingKiosk = new VotingKiosk();
        votingKioskCanNotVote = new VotingKiosk();
        incorrectPartyDB = new NoConnectivityToPartyDBStub();
        correctPartyDB = new CorrectConnectToPartyDBStub();
        teo = new ElectoralOrganismSpy();
        teoIncorrect = new ElectoralOrganismSpyCanNotVote();
        mst = new MailerServiceSpy();

        try {
            Nif nif = new Nif("12345678A");
            identityVerify = new IdentityVerifyCorrectMock(nif);
            mailAddress = new MailAddress("blabla@gmail.com");
            votingKiosk.setElectoralOrganism(teo);
            votingKioskCanNotVote.setElectoralOrganism(teoIncorrect);
            votingKiosk.setMailerService(mst);
            votingKioskCanNotVote.setMailerService(mst);
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
    void checkVoteExceptionsAreThrown() {
        assertThrows(AServiceNotInitializedException.class, () -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertThrows(NotValidPartyException.class, () -> votingKiosk.vote(null));
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.vote(party));
        assertFalse(teo.disableVoterCalled);
        assertFalse(teo.askedForSignature);
        assertFalse(teo.askedForCanVote);
    }

    @Test
    void checkCanNotVoteResponseFromElectoralOrganism() {
        assertDoesNotThrow(() -> votingKioskCanNotVote.setPartiesDB(correctPartyDB));
        assertDoesNotThrow(() -> votingKioskCanNotVote.startSession(identityVerify));
        assertThrows(VotingRightsFailedException.class, () -> votingKioskCanNotVote.vote(party));
        assertFalse(teoIncorrect.disableVoterCalled);
        assertFalse(teoIncorrect.askedForSignature);
        assertTrue(teoIncorrect.askedForCanVote);
    }

    @Test
    void checkVoteUsesElectoralOrganism() {
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertTrue(teo.disableVoterCalled);
        assertTrue(teo.askedForSignature);
        assertTrue(teo.askedForCanVote);
    }

    @Test
    void checkPartyDBExceptionWhenNotConnection() {
        assertThrows(NoConnectionToDBException.class, () -> votingKiosk.setPartiesDB(incorrectPartyDB));
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
    }

    @Test
    void checkThatExceptionIsThrownWhenNoVoteOrNoSessionAndAskedForMail() {
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertThrows(SessionNotStartedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertThrows(HasNotVotedException.class, () -> votingKiosk.sendeReceipt(mailAddress));
    }

    @Test
    void checkThatExceptionIsNotThrownWhenVoteAndAskedForMail() {
        assertDoesNotThrow(() -> votingKiosk.setPartiesDB(correctPartyDB));
        assertDoesNotThrow(() -> votingKiosk.startSession(identityVerify));
        assertDoesNotThrow(() -> votingKiosk.vote(party));
        assertDoesNotThrow(() -> votingKiosk.sendeReceipt(mailAddress));
        assertTrue(mst.emailSend);
    }
}
