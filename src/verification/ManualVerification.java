package verification;

import data.Nif;
import exceptions.verification.VerificationIdentityFailedException;
import exceptions.data.NotValidNifException;

import java.util.Scanner;

public class ManualVerification implements IdentityVerify {

    private int attempts;
    private Scanner scanner;
    private int validStaffId;
    private String password;

    public ManualVerification(Scanner scanner, int validStaffId, String password) {
        this.scanner = scanner;
        attempts = 0;
        this.validStaffId = validStaffId;
        this.password = password;
    }

    @Override
    public Nif getNif() throws VerificationIdentityFailedException {
        if(!askForLoginSupportStaff())
            throw new VerificationIdentityFailedException("The support staff member failed 3 times at logging in.");

        try {
            System.out.println("Write down the NIF verified: ");
            return new Nif(scanner.nextLine());
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

    private boolean logInSupportStaff() {
        System.out.println("A support staff member will verify your identity.");
        System.out.println("Please, wait until he or she comes.");
        scanner.nextLine();
        System.out.print("[userId]: ");
        int staffMemberId = scanner.nextInt();
        scanner.nextLine();
        //System.out.println(staffMemberId); For testing purposes
        System.out.print("[password]: ");
        String staffMemberPassword = scanner.nextLine();
        //System.out.println(staffMemberPassword); For testing purposes

        return staffMemberId == validStaffId && staffMemberPassword.equals(password);
    }
}
