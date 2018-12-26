package kiosk;

import data.*;
import java.util.Set;

public class VoteCounter {

        private Set<Party> parties;


        public VoteCounter(Set<Party> validParties) {
            this.parties = validParties;
        }
        public void countParty(Party party) {

        }
        public void countNull() {

        }
        public void countBlank() {

        }
        public void scrutinize(Party party) {

        }
        public int getVotesFor(Party party) {
            return 0;
        }
        public int getNulls() {
            return 0;
        }
        public int getBlanks() {
            return 0;
        }
        public int getTotal() {
            return 0;
        }
}

