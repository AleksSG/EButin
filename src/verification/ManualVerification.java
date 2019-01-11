package verification;

import data.Nif;
import exceptions.data.NotValidNifException;
import exceptions.VerificationIdentityFailedException;

public class ManualVerification implements IdentityVerify {

    private int attempts;

    public ManualVerification() {
        attempts = 0;
    }

    @Override
    public Nif getNif() throws VerificationIdentityFailedException {
        if(!askForLoginSupportStaff())
            throw new VerificationIdentityFailedException("The support staff member failed 3 times at logging in.");

        try {
            return getManualNif();
        }
        catch (NotValidNifException e) {
            throw new VerificationIdentityFailedException("The support staff member introduced a not valid NIF.");
        }
    }

    private boolean askForLoginSupportStaff() {
        for(; attempts < 3; attempts++) {
            if(logInSupportStaff())
                return true;
        }
        return false;
    }

    public boolean logInSupportStaff() {
        //TO-DO || Implemment an screen to make support staff member log in.

        return false;
    }

    public Nif getManualNif() throws NotValidNifException {
        //TO-DO || Implemment an screen to make the logged support staff member to write the Nif.
        throw new NotValidNifException("Nif wrong");
    }
}
