import data.Party;
import kiosk.VoteCounter;
import exceptions.InvalidNifException;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.fail;

class VoteCounterTest {

    private VoteCounter voteCounter;

    @BeforeEach
    void setUp() {
        Set<Party> validParties = new HashSet<>();
        validParties.add(new Party("PP"));
        validParties.add(new Party("Cs"));
        validParties.add(new Party("PSC"));
        voteCounter = new VoteCounter(validParties);

        //Vots nuls
        voteCounter.scrutinize(new Party("Null"));
        voteCounter.scrutinize(new Party("Invalid Party1"));
        voteCounter.scrutinize(new Party("Invalid Party2"));

        //Vots blancs
        voteCounter.scrutinize(new Party(""));
        voteCounter.scrutinize(new Party(""));

        //Vots valids
        voteCounter.scrutinize(new Party("PP"));
        voteCounter.scrutinize(new Party("Cs"));
        voteCounter.scrutinize(new Party("Cs"));
        voteCounter.scrutinize(new Party("PSC"));
        voteCounter.scrutinize(new Party("PSC"));
        voteCounter.scrutinize(new Party("PSC"));
    }

    @Test
    void getNullsTest() {
        try {
            Assertions.assertEquals(3, voteCounter.getNulls());
            voteCounter.scrutinize(new Party("Podemos"));
            voteCounter.scrutinize(new Party(""));
            Assertions.assertEquals(4, voteCounter.getNulls());
        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    void getBlanksTest() {
        try {
            Assertions.assertEquals(2, voteCounter.getBlanks());
            voteCounter.scrutinize(new Party("Podemos"));
            voteCounter.scrutinize(new Party(""));
            Assertions.assertEquals(3, voteCounter.getBlanks());
        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    void getPartyTest() {
        try {
            Assertions.assertEquals(1, voteCounter.getVotesFor(new Party("PP")));
            Assertions.assertEquals(2, voteCounter.getVotesFor(new Party("Cs")));
            Assertions.assertEquals(3, voteCounter.getVotesFor(new Party("PSC")));
            voteCounter.scrutinize(new Party("PP"));
            voteCounter.scrutinize(new Party("PP"));
            Assertions.assertEquals(3, voteCounter.getVotesFor(new Party("PP")));
        }
        catch(Exception e) {
            fail();
        }
    }

    @Test
    void getTotalVotes() {
        try {
            Assertions.assertEquals(6, voteCounter.getTotal());
            voteCounter.scrutinize(new Party("PP"));
            voteCounter.scrutinize(new Party("PP"));
            Assertions.assertEquals(8, voteCounter.getTotal());
        }
        catch(Exception e) {
            fail();
        }
    }

}
