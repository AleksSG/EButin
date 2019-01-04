import data.Party;
import exceptions.NotValidPartyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class PartyTest {
    private String party;

    @BeforeEach
    void setUp(){
        party = "VOX";
    }

    @Test
    @DisplayName("Check Party validation")
    void validPartyTest() {
        Assertions.assertDoesNotThrow(() -> new Party(party));
        Assertions.assertThrows(NotValidPartyException.class, () -> new Party(null));
    }

    @Test
    @DisplayName("Check Party getName")
    void getNameTest() {
        try {
            Assertions.assertEquals(party, new Party(party).getName());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
