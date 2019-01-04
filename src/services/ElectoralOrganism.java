package services;

import data.*;
import exceptions.NotValidDigitalSignatureException;

public interface ElectoralOrganism {
    boolean canVote(Nif nif);
    void disableVoter(Nif nif);
    DigitalSignature askForDigitalSignature(Party party);
}
