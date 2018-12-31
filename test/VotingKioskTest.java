import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.NotValidDigitalSignatureException;
import exceptions.NotValidMailException;
import exceptions.NotValidNifException;
import exceptions.NotValidPartyException;
import kiosk.VotingKiosk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.ElectoralOrganism;
import services.MailerService;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VotingKioskTest {

    private VotingKiosk votingKiosk;

    @BeforeEach
    void setUp() {
        votingKiosk = new VotingKioskDouble();
    }

    @Test
    void voteTest() {


    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void ElectoralOrganismTesting() {
        ElectoralOrganism teo = new TestElectoralOrganism();
        try {
            assertTrue(teo.canVote(new Nif("12345678A")), "Valid NIF can vote");
            assertFalse(teo.canVote(new Nif("23456789A")), "Valid NIF cannot vote");

            teo.disableVoter(new Nif("12345678A"));
            assertFalse(teo.canVote(new Nif("12345678A")), "Same NIF as before, now cannot vote");

            assertEquals(new DigitalSignature(new byte[]{1,2,3,4}), teo.askForDigitalSignature(new Party("")));
        } catch (NotValidNifException | NotValidPartyException | NotValidDigitalSignatureException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Electoral Organism Double Test")
    void MailerServiceTest() {
        TestMailerService mst = new TestMailerService();
        try {
            mst.send(new MailAddress("prova@gmail.com"), new DigitalSignature(new byte[]{}));
            assertTrue(mst.isSend(), "Check if send");
        } catch (NotValidDigitalSignatureException | NotValidMailException e) {
            fail();
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

        @Override
        public boolean canVote(Nif nif) {
            try {
                createSet();
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
            return new byte[]{1, 2, 3, 4};
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
}
