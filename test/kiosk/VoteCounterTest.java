package kiosk;

import data.Party;
import kiosk.VoteCounter;
import org.junit.jupiter.api.*;

import java.util.Set;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.fail;

class VoteCounterTest {

    private VoteCounter voteCounter;

    @BeforeEach
    void setUp() {
        Set<Party> validParties = new HashSet<>();
        try {
            validParties.add(new Party("PP"));
            validParties.add(new Party("Cs"));
            validParties.add(new Party("PSC"));
            voteCounter = new VoteCounter(validParties);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //Vots nuls
        try {
            voteCounter.scrutinize(new Party("null"));
            voteCounter.scrutinize(new Party("Invalid Party1"));
            voteCounter.scrutinize(new Party("Invalid Party2"));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //Vots blancs
        try {
            voteCounter.scrutinize(new Party(""));
            voteCounter.scrutinize(new Party(""));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //Vots valids
        try {
            voteCounter.scrutinize(new Party("PP"));
            voteCounter.scrutinize(new Party("Cs"));
            voteCounter.scrutinize(new Party("Cs"));
            voteCounter.scrutinize(new Party("PSC"));
            voteCounter.scrutinize(new Party("PSC"));
            voteCounter.scrutinize(new Party("PSC"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
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
            fail(e.getMessage());
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
            fail(e.getMessage());
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
            fail(e.getMessage());
        }
    }

    @Test
    void getTotalVotes() {
        try {
            Assertions.assertEquals(11, voteCounter.getTotal());
            voteCounter.scrutinize(new Party("PP"));
            voteCounter.scrutinize(new Party("PP"));
            Assertions.assertEquals(13, voteCounter.getTotal());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }

}
