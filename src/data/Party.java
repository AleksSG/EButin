package data;

import utils.VotePartyCounter;

public final class Party extends VotePartyCounter {
    private final String name;

    public Party(String name) throws  NullPointerException {
        if(name == null)
            throw new NullPointerException("Party name can't be null");
        this.name = name;
        super.partyVotes = 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Party party1 = (Party) o;
        return name.equals(party1.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Party{" + "name='" + name + '\'' + '}';
    }
}
