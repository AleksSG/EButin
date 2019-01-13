package verification;

import data.Nif;
import exceptions.verification.VerificationIdentityFailedException;
import exceptions.data.NotValidNifException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ManualVerificationTest {

    private IdentityVerify manualVerificationNotValidStaff;
    private IdentityVerify manualVerificationValidStaffWrongNif;
    private IdentityVerify manualVerificationValidStaffAt3rdAttemptRightNif;

    @BeforeEach
    void setUp() {
        manualVerificationNotValidStaff = new ManualVerification(
                new Scanner("\n22\nroot\n\n23\nROOT\n\n23\nadasd\n\n23\nroot\n12345678A\n"),
                23,
                "root"
        );

        manualVerificationValidStaffWrongNif = new ManualVerification(
                new Scanner("\n23\nroot\n12345ASD\n"),
                23,
                "root"
        );

        manualVerificationValidStaffAt3rdAttemptRightNif = new ManualVerification(
                new Scanner("\n22\nroot\n\n23\nROOT\n\n23\nroot\n12345678A\n"),
                23,
                "root"
        );
    }

    @Test
    void getNifTest() {
        assertThrows(VerificationIdentityFailedException.class, () -> manualVerificationNotValidStaff.getNif());
        assertThrows(VerificationIdentityFailedException.class, () -> manualVerificationValidStaffWrongNif.getNif());
        try {
            assertEquals(new Nif("12345678A"), manualVerificationValidStaffAt3rdAttemptRightNif.getNif());
        } catch (NotValidNifException | VerificationIdentityFailedException e) {
            e.printStackTrace();
            fail();
        }

    }
}
