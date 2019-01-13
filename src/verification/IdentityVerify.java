package verification;

import data.Nif;
import exceptions.verification.VerificationIdentityFailedException;

public interface IdentityVerify {
    Nif getNif() throws VerificationIdentityFailedException;
}
