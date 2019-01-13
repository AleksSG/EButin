package kiosk;

import data.*;
import exceptions.kiosk.NotValidSetOfPartiesException;
import exceptions.kiosk.PartyNotFoundException;

import java.util.HashMap;
import java.util.Set;

class VoteCounter {

    private final HashMap<Party, PartyVoteCounter> partiesVoteCounter;

    private final PartyVoteCounter numVotes;
    private final PartyVoteCounter numBlanks;
    private final PartyVoteCounter numNulls;


    VoteCounter(Set<Party> validParties) throws NotValidSetOfPartiesException {
        if (validParties == null || validParties.isEmpty())
            throw new NotValidSetOfPartiesException();

        partiesVoteCounter = new HashMap<>();
        validParties.forEach(party -> partiesVoteCounter.put(party, new PartyVoteCounter()));

        numVotes = new PartyVoteCounter();
        numBlanks = new PartyVoteCounter();
        numNulls = new PartyVoteCounter();
    }

    private void countParty(Party party) {
        partiesVoteCounter.get(party).increment();
        numVotes.increment();
    }

    private void countNull() {
        numNulls.increment();
        numVotes.increment();
    }

    private void countBlank() {
        numBlanks.increment();
        numVotes.increment();
    }

    void scrutinize(Party party) {
        if (party == null)
            throw new NullPointerException("The parameter party mustn't be null");

        if (party.isEmpty())
            countBlank();
        else if (party.isNull() || !partiesVoteCounter.containsKey(party))
            countNull();
        else
            countParty(party);
    }

    int getVotesFor(Party party) throws PartyNotFoundException {
        PartyVoteCounter counter = partiesVoteCounter.get(party);
        if (counter == null)
            throw new PartyNotFoundException();

        return counter.getVotes();
    }

    int getNulls() {
        return numNulls.getVotes();
    }

    int getBlanks() {
        return numBlanks.getVotes();
    }

    int getTotal() {
        return numVotes.getVotes();
    }

    Set<Party> getValidParties() {
        return partiesVoteCounter.keySet();
    }

    static final class PartyVoteCounter {
        private int numVotes;

        PartyVoteCounter() {
            numVotes = 0;
        }

        void increment() {
            numVotes++;
        }

        int getVotes() {
            return numVotes;
        }
    }

}

