package services;

import data.DigitalSignature;
import data.MailAddress;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MailServiceTest {

    private MailerServiceImplementation mailerServiceImplementation;

    @BeforeEach
    void setUp() {
        mailerServiceImplementation = new MailerServiceImplementation();
    }


    @Test
    void checkNotThrowsGettingPartiesFromDB() {
        assertDoesNotThrow(() -> mailerServiceImplementation.send(new MailAddress("asas@asas.asas"), new DigitalSignature(new byte[32])));
    }
}
