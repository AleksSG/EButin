package services;

import data.Nif;
import data.Party;
import exceptions.data.NotValidNifException;
import exceptions.data.NotValidPartyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectoralOrganismTest {

    private ElectoralOrganism electoralOrganismImplementation;
    private Nif validNif, notValidNif;
    private Party party;

    @BeforeEach
    void setUp() {
        electoralOrganismImplementation = new ElectoralOrganismImplementation();
        try {
            validNif = new Nif("12345678A");
            notValidNif = new Nif("12345678B");
        } catch (NotValidNifException e) {
            e.printStackTrace();
            fail();
        }

        try {
            party = new Party("PP");
        } catch (NotValidPartyException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    void checkCanVoteAndDisableVoter() {
        assertFalse(electoralOrganismImplementation.canVote(notValidNif));
        assertTrue(electoralOrganismImplementation.canVote(validNif));
        electoralOrganismImplementation.disableVoter(validNif);
        assertFalse(electoralOrganismImplementation.canVote(validNif));
    }

    @Test
    void checkAskForDigitalSignature() {
        assertNotNull(electoralOrganismImplementation.askForDigitalSignature(party));
    }
}
