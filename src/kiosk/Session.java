package kiosk;

import data.DigitalSignature;
import data.Nif;
import exceptions.kiosk.HasNotVotedException;
import exceptions.data.NotValidDigitalSignatureException;
import exceptions.verification.VerificationIdentityFailedException;
import verification.IdentityVerify;

class Session {
    private Nif nif;
    private DigitalSignature digitalSignature;

    Session(IdentityVerify identityVerify) throws VerificationIdentityFailedException {
        if(identityVerify == null)
            throw new VerificationIdentityFailedException("This parameter can't be null");

        this.nif = identityVerify.getNif();
        digitalSignature = null;
    }

    void setDigitalSignature(DigitalSignature digitalSignature) throws NotValidDigitalSignatureException {
        if(digitalSignature == null)
            throw new NotValidDigitalSignatureException();

        this.digitalSignature = digitalSignature;
    }

    DigitalSignature getDigitalSignature() throws HasNotVotedException {
        if(digitalSignature == null)
            throw new HasNotVotedException();

        return digitalSignature;
    }

    Nif getNif() {
        return nif;
    }
}
