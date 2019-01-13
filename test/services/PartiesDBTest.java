package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PartiesDBTest {

    private PartiesDB partiesDBImplementation;

    @BeforeEach
    void setUp() {
        partiesDBImplementation = new PartiesDBImplementation();
    }

    @Test
    void checkNotThrowsGettingPartiesFromDB() {
        assertDoesNotThrow(() -> partiesDBImplementation.getPartiesFromDB());
    }
}
