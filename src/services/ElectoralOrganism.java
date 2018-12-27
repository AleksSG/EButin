package services;

import data.*;

public interface ElectoralOrganism {
    boolean canVote(Nif nif);
    void disableVoter(Nif nif);
    DigitalSignature askForDigitalSignature(Party party);
}
