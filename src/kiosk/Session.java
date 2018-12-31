package kiosk;

import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;

public class Session {
    private Nif nif;
    private DigitalSignature digitalSignature;

    public Session(Nif nif) {
        this.nif = nif;
        digitalSignature = null;
    }

    public void setDigitalSignature(DigitalSignature digitalSignature) {
        this.digitalSignature = digitalSignature;
    }

    public DigitalSignature getDigitalSignature() throws HasNotVotedException {
        if(digitalSignature == null)
            throw new HasNotVotedException();

        return digitalSignature;
    }

    public Nif getNif() {
        return nif;
    }
}
