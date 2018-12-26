package utils;

public class VotePartyCounter {
    protected int partyVotes;

    public int getPartyVotes() { return partyVotes; }
    public void countVote() {
        partyVotes++;
    }
}
