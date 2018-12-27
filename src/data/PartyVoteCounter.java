package data;

public final class PartyVoteCounter {
    private int numVotes;

    public PartyVoteCounter() {
        numVotes = 0;
    }

    public void increment() {
        numVotes++;
    }

    public int getVotes() {
        return numVotes;
    }
}
