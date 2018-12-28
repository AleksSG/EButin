import data.DigitalSignature;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import org.junit.*;
import services.ElectoralOrganism;

import java.util.ArrayList;

class VotingKioskTest {


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

    private 
}
