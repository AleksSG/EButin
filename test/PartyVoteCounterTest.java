import kiosk.VoteCounter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PartyVoteCounterTest {
    private VoteCounter.PartyVoteCounter counter;

    @BeforeEach
    void setUp(){
        counter = new VoteCounter.PartyVoteCounter();
    }

    @Test
    @DisplayName("Check increment and get")
    void partyVoteCounterTest() {
        Assertions.assertEquals(0, counter.getVotes());
        counter.increment();
        Assertions.assertEquals(1, counter.getVotes());
        counter.increment();
        Assertions.assertEquals(2, counter.getVotes());
    }
}
