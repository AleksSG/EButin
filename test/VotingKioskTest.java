import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.NotValidDigitalSignatureException;
import exceptions.NotValidNifException;
import exceptions.NotValidPartyException;
import exceptions.NotValidSetOfPartiesException;
import kiosk.VotingKiosk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ElectoralOrganism;
import services.MailerService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VotingKioskTest {

    private VotingKiosk votingKiosk;

    @BeforeEach
    void setUp() {
        try {
            votingKiosk = new VotingKiosk();
        } catch (NotValidSetOfPartiesException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void ElectoralOrganismTesting() {
        ElectoralOrganism teo = new TestElectoralOrganism();
        try {
            assertTrue(teo.canVote(new Nif("12345678A")), "Valid NIF can vote");
            assertFalse(teo.canVote(new Nif("23456789A")), "Valid NIF cannot vote");

            teo.disableVoter(new Nif("12345678A"));
            //assertEquals(false, teo.canVote(new Nif("12345678A")), "Same NIF as before, now cannot vote");

            assertEquals(new DigitalSignature(new byte[]{1,2,3,4}), teo.askForDigitalSignature(new Party("")));
        } catch (NotValidNifException | NotValidPartyException | NotValidDigitalSignatureException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void MailerServiceTest() {
        MailerService mst = new TestMailerService();
    }


    private class TestElectoralOrganism implements ElectoralOrganism {

        private ArrayList<Nif> canVoteNif;

        @Override
        public boolean canVote(Nif nif) {
            try {
                createArray();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return canVoteNif.contains(nif);
        }

        @Override
        public void disableVoter(Nif nif) {
            canVoteNif.remove(nif);
        }

        private void createArray() throws NotValidNifException {
            canVoteNif = new ArrayList<>();
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
            return new byte[]{1, 2, 3, 4};
        }
    }

    private class TestMailerService implements MailerService {
        private boolean emailSend = false;
        @Override
        public void send(MailAddress address, DigitalSignature signature) {
            this.emailSend = true;
        }

        public boolean isSend(){
            return this.emailSend;
        }
    }
}
