import data.DigitalSignature;
import data.Nif;
import exceptions.NotValidDigitalSignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class DigitalSignatureTest {
    private byte[] digitalSignature, notValidDigitalSignature;

    @BeforeEach
    void setUp(){
        digitalSignature = new byte[]{ 127, 0, 0, 1 };
        notValidDigitalSignature = new byte[33];
    }

    @Test
    @DisplayName("Check digitalSignature validation")
    void validDigitalSignatureTest() {
        Assertions.assertDoesNotThrow(() -> new DigitalSignature(digitalSignature));
        Assertions.assertThrows(NotValidDigitalSignatureException.class, () -> new DigitalSignature(null));
        Assertions.assertThrows(NotValidDigitalSignatureException.class, () -> new DigitalSignature(notValidDigitalSignature));
    }

    @Test
    @DisplayName("Check DigitalSignature getVotingOption")
    void getVotingOptionTest() {
        try {
            Assertions.assertEquals(digitalSignature, new DigitalSignature(digitalSignature).getVotingOption());
        }
        catch(Exception e) {
            fail(e.getMessage());
        }
    }
}
