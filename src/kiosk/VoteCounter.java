package kiosk;

import data.*;

import java.util.Iterator;
import java.util.Set;

public class VoteCounter {

        private Set<Party> parties;
        private int total;
        private int blank;
        private int nuls;


        public VoteCounter(Set<Party> validParties) {
            this.parties = validParties;
            total= 0;
        }

        public void countParty(Party party) {
            party.countVote();
        }
        public void countNull() {
            this.nuls++;
        }

        public void countBlank() {
            this.blank++;
        }

        public void scrutinize(Party party) {
            if (new Party("").equals(party)) {
                countBlank();
            } else if (party == null || !parties.contains(party)) {
                countNull();
            } else {
                countParty(party);
            }
            total++;
        }

        public int getVotesFor(Party party) {
            if (parties.contains(party)) {
                return party.getPartyVotes();
            }
            return 0;
        }
        public int getNulls() {
            return this.nuls;
        }
        public int getBlanks() {
            return this.blank;
        }
        public int getTotal() {
            total = 0;
            Iterator<Party> it = parties.iterator();
            while (it.hasNext()) {
                total += it.next().getPartyVotes();
            }
            return total;
        }
}

