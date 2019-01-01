package kiosk;

import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;
import exceptions.NotValidDigitalSignatureException;
import exceptions.NotValidNifException;

public class Session {
    private Nif nif;
    private DigitalSignature digitalSignature;

    public Session(Nif nif) throws NotValidNifException {
        if(nif == null)
            throw new NotValidNifException("Nif can't be null");

        this.nif = nif;
        digitalSignature = null;
    }

    public void setDigitalSignature(DigitalSignature digitalSignature) throws NotValidDigitalSignatureException {
        if(digitalSignature == null)
            throw new NotValidDigitalSignatureException();

        this.digitalSignature = digitalSignature;
    }

    public DigitalSignature getDigitalSignature() throws HasNotVotedException {
        if(digitalSignature == null)
            throw new HasNotVotedException();

        return digitalSignature;
    }

    Nif getNif() {
        return nif;
    }
}
