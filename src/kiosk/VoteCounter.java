package kiosk;

import data.*;
import exceptions.InvalidSetOfPartiesException;
import exceptions.PartyNotFoundException;

import java.util.HashMap;
import java.util.Set;

public class VoteCounter {

        private HashMap<Party, PartyVoteCounter> partiesVoteCounter;

        private int numVotes;
        private int numBlanks;
        private int numNulls;


        public VoteCounter(Set<Party> validParties) throws InvalidSetOfPartiesException {
            if(validParties == null || validParties.isEmpty())
                throw new InvalidSetOfPartiesException();

            partiesVoteCounter = new HashMap<>();
            validParties.forEach(party -> partiesVoteCounter.put(party, new PartyVoteCounter()));

            numVotes = 0;
            numBlanks = 0;
            numNulls = 0;
        }

        private void countParty(Party party) {
            partiesVoteCounter.get(party).increment();
        }

        private void countNull() {
            numNulls++;
        }

        private void countBlank() {
            numBlanks++;
        }

        public void scrutinize(Party party) {
            if (new Party("").equals(party)) {
                countBlank();
            } else if (party == null || !partiesVoteCounter.containsKey(party)) {
                countNull();
            } else {
                countParty(party);
            }
            numVotes++;
        }

        public int getVotesFor(Party party) throws PartyNotFoundException {
            PartyVoteCounter counter = partiesVoteCounter.get(party);
            if(counter == null)
                throw new PartyNotFoundException();

            return counter.getVotes();
        }

        public int getNulls() {
            return numNulls;
        }

        public int getBlanks() {
            return numBlanks;
        }

        public int getTotal() {
            return numVotes;
        }
}

