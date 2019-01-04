package verification;

import data.Nif;
import exceptions.VerificationIdentityFailedException;

public interface IdentityVerify {
    Nif getNif() throws VerificationIdentityFailedException;
}
