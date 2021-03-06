import biometric.BiometricReaderImplementation;
import biometric.BiometricScannerImplementation;
import biometric.BiometricSoftwareImplementation;
import data.BiometricData;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.data.*;
import exceptions.kiosk.*;
import exceptions.services.NoConnectionToDBException;
import exceptions.verification.VerificationIdentityFailedException;
import kiosk.VotingKiosk;
import services.ElectoralOrganismImplementation;
import services.MailerServiceImplementation;
import services.PartiesDBImplementation;
import verification.BiometricVerification;
import verification.IdentityVerify;
import verification.ManualVerification;

import java.math.BigInteger;
import java.util.Scanner;

public class TerminalDeVot {
    private static final int VALID_STAFF_ID = 23;
    private static final String VALID_STAFF_PASSWORD = "root";

    private static final String NIF_READ_FROM_PASSPORT = "12345678A";

    //Uncomment/Comment those in order to fail/success the biometric method and start the alternative (manual method).
    private static final BigInteger FACE_NUMBER_SCANNED = new BigInteger("42332421478654387653826348236023875602365023874967948365348765348766"); //fails
    //private static final BigInteger FACE_NUMBER_SCANNED = new BigInteger("12332421478654387653826348236023875602365023874967948365348765348766"); //success

    private static final BigInteger FINGER_NUMBER_SCANNED = new BigInteger("43534534634534348765398642084762370856342875647353784650347453485348");
    private static final BigInteger FACE_NUMBER_PASSPORT = new BigInteger("12332074637085634950873632074650748365038465940984873504736348734853");
    private static final BigInteger FINGER_NUMBER_PASSPORT = new BigInteger("43537860843765048730856278068047362084726478623580635748658473620831");

    public static void main(String[] args) {

        //Initialize the reader from system input
        Scanner scanner = new Scanner(System.in);

        //Initialize VotingKiosk instance
        VotingKiosk votingKiosk = new VotingKiosk();

        setServices(votingKiosk);

        System.out.print("Write 'start' in order to start a voting process: ");
        startVotingProcess(votingKiosk, scanner);
        System.out.println("\nBYE!!!");
    }

    private static void setServices(VotingKiosk votingKiosk) {
        try {
            votingKiosk.setPartiesDB(new PartiesDBImplementation());
        } catch (NoConnectionToDBException | NotValidSetOfPartiesException e) {
            e.printStackTrace();
            return;
        }
        votingKiosk.setElectoralOrganism(new ElectoralOrganismImplementation());
        votingKiosk.setMailerService(new MailerServiceImplementation());
    }

    private static void startVotingProcess(VotingKiosk votingKiosk, Scanner scanner) {
        while(scanner.nextLine().equals("start")) {
            System.out.println("[VotingProcess] START\n");
            votingProcess(votingKiosk, scanner);
            System.out.println("\n[VotingProcess] END");

            System.out.print("\n\nWrite 'start' in order to start a voting process: ");
        }
    }

    private static void votingProcess(VotingKiosk votingKiosk, Scanner scanner) {
        System.out.println("A new voting process has been started!");

        IdentityVerify identityVerify = askForConsent(scanner) ? getBiometricVerificationProcess(scanner) : getManualVerificationProcess(scanner);

        startSession(votingKiosk, identityVerify);

        displayValidParties(votingKiosk);
        vote(votingKiosk, askForAPartyToVote(scanner));

        if(askForEReceipt(scanner)) {
            sendEmail(votingKiosk, scanner);
        }

        votingKiosk.closeSession();

        System.out.println("Thanks for voting using this app! See you!");
    }

    private static void startSession(VotingKiosk votingKiosk, IdentityVerify identityVerify) {
        try {
            votingKiosk.startSession(identityVerify);
        } catch (SessionNotFinishedException | VerificationIdentityFailedException e) {
            System.out.println(e.getMessage());
            return;
        }
    }

    private static boolean askForConsent(Scanner scanner) {
        System.out.print("Do you want to give us your consent about using your Passport Biometric Data? (Y/N): ");

        return scanner.nextLine().toLowerCase().equals("y");
    }

    private static void vote(VotingKiosk votingKiosk, Party selectedParty) {
        try {
            votingKiosk.vote(selectedParty);
            System.out.println("Vote has been sent successfully!");
        } catch (NullPointerException | SessionNotStartedException | NotValidDigitalSignatureException | VotingRightsFailedException | NotValidPartyException | AServiceNotInitializedException e) {
            e.printStackTrace();
            votingKiosk.closeSession();
            return;
        }
    }

    private static void sendEmail(VotingKiosk votingKiosk, Scanner scanner) {
        try {
            votingKiosk.sendeReceipt(askForEMail(scanner));
        } catch (SessionNotStartedException | HasNotVotedException e) {
            e.printStackTrace();
            votingKiosk.closeSession();
            return;
        }
    }

    private static ManualVerification getManualVerificationProcess(Scanner scanner) {
        return new ManualVerification(scanner, VALID_STAFF_ID, VALID_STAFF_PASSWORD);
    }

    private static BiometricVerification getBiometricVerificationProcess(Scanner scanner) {
        Nif nif = null;
        BiometricData dataRead = null;

        try {
            nif = new Nif(NIF_READ_FROM_PASSPORT);
            dataRead = new BiometricData(FACE_NUMBER_PASSPORT, FINGER_NUMBER_PASSPORT);
        } catch (NotValidNifException | NotValidBiometricDataException e) {
            e.printStackTrace();
        }

        return new BiometricVerification(nif, new BiometricSoftwareImplementation(new BiometricReaderImplementation(dataRead), new BiometricScannerImplementation(FACE_NUMBER_SCANNED, FINGER_NUMBER_SCANNED)), getManualVerificationProcess(scanner));
    }

    private static void displayValidParties(VotingKiosk votingKiosk) {
        System.out.println("List of Valid Parties: ");
        votingKiosk.getPartiesFromVoteCounter().forEach(v -> System.out.println("\t" + v.getName()));
        System.out.println();
    }

    private static Party askForAPartyToVote(Scanner scanner) {
        Party selectedParty = null;
        while(selectedParty == null) {
            System.out.print("Write down the name of the party you want to vote or press ENTER key to send a protest vote (it is case sensitive): ");
            String name = scanner.nextLine();
            System.out.print("Are you sure you want to vote " + name + "? (Y/N): ");
            if(scanner.nextLine().toLowerCase().equals("y")) {
                try {
                    selectedParty = new Party(name);
                } catch (NotValidPartyException e) {
                    e.printStackTrace();
                }
            }
        }
        return selectedParty;
    }

    private static boolean askForEReceipt(Scanner scanner) {
        System.out.print("Do you want to get an e-receipt? (Y/N): ");

        return scanner.nextLine().toLowerCase().equals("y");
    }

    private static MailAddress askForEMail(Scanner scanner) {
        MailAddress selectedMailAddress = null;
        while(selectedMailAddress == null) {
            System.out.print("Write down your e-mail adress: ");
            String name = scanner.nextLine();
            System.out.print("Are you sure '" + name + "' is your e-mail address? (Y/N): ");
            if(scanner.nextLine().toLowerCase().equals("y")) {
                try {
                    selectedMailAddress = new MailAddress(name);
                } catch (NotValidMailException e) {
                    e.printStackTrace();
                }
            }
        }
        return selectedMailAddress;
    }
}
