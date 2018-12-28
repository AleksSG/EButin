import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import exceptions.InvalidSetOfPartiesException;
import kiosk.VotingKiosk;
import org.junit.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ElectoralOrganism;
import services.MailerService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class VotingKioskTest {

    private VotingKiosk votingKiosk;

    @Before
    void setUp() {
        try {
            votingKiosk = new VotingKiosk();
        } catch (InvalidSetOfPartiesException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void ElectoralOrganismTesting() {
        ElectoralOrganism teo = new TestElectoralOrganism();
        try {
            Assert.assertEquals("Valid NIF can vote",true, teo.canVote(new Nif("12345678A")));
            Assert.assertEquals("Valid NIF cannot vote", false, teo.canVote(new Nif("23456789A")));

            teo.disableVoter(new Nif("12345678A"));
            //Assert.assertEquals("Same NIF as before, now cannot vote",false, teo.canVote(new Nif("12345678A")));

            Assert.assertEquals(new DigitalSignature(new byte[]{1,2,3,4}), teo.askForDigitalSignature(new Party("")));
        } catch (InvalidNifException e) {
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
                return canVoteNif.contains(nif);
            } catch (InvalidNifException e) {
                return false;
            }
        }

        @Override
        public void disableVoter(Nif nif) {
            try {
                createArray();
                if (canVoteNif.contains(nif))
                    canVoteNif.remove(nif);
            } catch (InvalidNifException e) {
            }
        }

        private void createArray() throws InvalidNifException{
            canVoteNif = new ArrayList<>();
            canVoteNif.add(new Nif("12345678A"));
            canVoteNif.add(new Nif("A1234567A"));
        }

        @Override
        public DigitalSignature askForDigitalSignature(Party party) {
            return new DigitalSignature(cifrarOpcioVot(party));
        }

        private byte[] cifrarOpcioVot(Party party) {
            byte[] codiCompr = {1, 2, 3, 4};
            return codiCompr;
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
