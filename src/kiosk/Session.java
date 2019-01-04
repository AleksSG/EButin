package kiosk;

import data.DigitalSignature;
import data.Nif;
import exceptions.HasNotVotedException;
import exceptions.NotValidDigitalSignatureException;
import exceptions.VerificationIdentityFailedException;
import verification.IdentityVerify;

public class Session {
    private Nif nif;
    private DigitalSignature digitalSignature;

    public Session(IdentityVerify identityVerify) throws VerificationIdentityFailedException {
        if(identityVerify == null)
            throw new VerificationIdentityFailedException("This parameter can't be null");

        this.nif = identityVerify.getNif();
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
