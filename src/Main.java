import biometric.BiometricReader;
import biometric.BiometricReaderImplementation;
import biometric.BiometricScannerImplementation;
import biometric.BiometricSoftwareImplementation;
import data.*;
import exceptions.*;
import exceptions.data.*;
import kiosk.VotingKiosk;
import services.ElectoralOrganism;
import verification.BiometricVerification;
import verification.IdentityVerify;
import verification.ManualVerification;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final int VALID_STAFF_ID = 23;
    private static final String VALID_STAFF_PASSWORD = "root";

    private static final String NIF_READ_FROM_PASSPORT = "12345678A";

    //Uncomment/Comment those in order to fail/success the biometric method and start the alternative (manual method).
    private static final BigInteger FACE_NUMBER_SCANNED = new BigInteger("42332421478654387653826348236023875602365023874967948365348765348766"); //fails
    //private static final BigInteger FACE_NUMBER_SCANNED = new BigInteger("12332421478654387653826348236023875602365023874967948365348765348766"); //success

    private static final BigInteger FINGER_NUMBER_SCANNED = new BigInteger("43534534634534348765398642084762370856342875647353784650347453485348");
    private static final BigInteger FACE_NUMBER_PASSPORT = new BigInteger("12332074637085634950873632074650748365038465940984873504736348734853");
    private static final BigInteger FINGER_NUMBER_PASSPORT = new BigInteger("43537860843765048730856278068047362084726478623580635748658473620831");

    private static Scanner scanner;
    private static VotingKiosk votingKiosk;

    public static void main(String[] args) {

        //Initialize the reader from system input
        scanner = new Scanner(System.in);

        //Initialize VotingKiosk instance
        votingKiosk = new VotingKiosk(){
            public Set<Party> getPartiesFromDB(){
                return new HashSet<Party>() {{
                    try {
                        add(new Party("Cs"));
                        add(new Party("JxCAT"));
                        add(new Party("ERC"));
                        add(new Party("PSC"));
                        add(new Party("COMÚ PODEM"));
                        add(new Party("CUP"));
                        add(new Party("PP"));
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }};
            }
        };

        votingKiosk.setElectoralOrganism(new ElectoralOrganism() {
            @Override
            public boolean canVote(Nif nif) {
                return true;
            }

            @Override
            public void disableVoter(Nif nif) {

            }

            @Override
            public DigitalSignature askForDigitalSignature(Party party) {
                DigitalSignature digitalSignature = null;
                try {
                    digitalSignature = new DigitalSignature(new byte[32]);
                } catch (NotValidDigitalSignatureException e) {
                    e.printStackTrace();
                }
                return digitalSignature;
            }
        });

        votingKiosk.setMailerService((address, signature) -> System.out.println("A mail has been sent to " + address.getMail()));

        System.out.print("Write 'start' in order to start a voting process: ");
        while(scanner.nextLine().equals("start")) {
            System.out.println("[VotingProcess] START\n");
            votingProcess();
            System.out.println("\n[VotingProcess] END");

            System.out.print("\n\nWrite 'start' in order to start a voting process: ");
        }

        System.out.println("\nBYE!!!");
    }

    private static void votingProcess() {
        System.out.println("A new voting process has been started!");

        IdentityVerify identityVerify = askForConsent() ? getBiometricVerificationProcess() : getManualVerificationProcess();

        try {
            votingKiosk.startSession(identityVerify);
        } catch (SessionNotFinishedException | VerificationIdentityFailedException e) {
            System.out.println(e.getMessage());
            return;
        }

        displayValidParties();
        Party selectedParty = askForAPartyToVote();
        try {
            votingKiosk.vote(selectedParty);
        } catch (NullPointerException | SessionNotStartedException | NotValidDigitalSignatureException | VotingRightsFailedException e) {
            System.out.println(e.getMessage());
            try {
                votingKiosk.closeSession();
            } catch (SessionNotStartedException e1) {
                e1.printStackTrace();
            }
            return;
        }

        System.out.println("Vote has been sent successfully!");

        if(askForEReceipt()) {
            try {
                votingKiosk.sendeReceipt(askForEMail());
            } catch (SessionNotStartedException | HasNotVotedException e) {
                e.printStackTrace();
                try {
                    votingKiosk.closeSession();
                } catch (SessionNotStartedException e1) {
                    e1.printStackTrace();
                }
                return;
            }
        }

        try {
            votingKiosk.closeSession();
        } catch (SessionNotStartedException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Thanks for voting using this app! See you!");
    }

    private static boolean askForConsent() {
        System.out.print("Do you want to give us your consent about using your Passport Biometric Data? (Y/N): ");

        return scanner.nextLine().toLowerCase().equals("y");
    }

    private static ManualVerification getManualVerificationProcess() {
        return new ManualVerification(scanner, VALID_STAFF_ID, VALID_STAFF_PASSWORD);
    }

    private static BiometricVerification getBiometricVerificationProcess() {
        Nif nif = null;
        BiometricData dataRead = null;

        try {
            nif = new Nif(NIF_READ_FROM_PASSPORT);
            dataRead = new BiometricData(FACE_NUMBER_PASSPORT, FINGER_NUMBER_PASSPORT);
        } catch (NotValidNifException | NotValidBiometricDataException e) {
            e.printStackTrace();
        }

        return new BiometricVerification(nif, new BiometricSoftwareImplementation(new BiometricReaderImplementation(dataRead), new BiometricScannerImplementation(FACE_NUMBER_SCANNED, FINGER_NUMBER_SCANNED)), getManualVerificationProcess());
    }

    private static void displayValidParties() {
        System.out.println("List of Valid Parties: ");
        votingKiosk.getPartiesFromVoteCounter().forEach(v -> System.out.println("\t" + v.getName()));
        System.out.println();
    }

    private static Party askForAPartyToVote() {
        Party selectedParty = null;
        while(selectedParty == null) {
            System.out.print("Write down the name of the party you want to vote or press ENTER key to send a protest vote (is case sensitive): ");
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

    private static boolean askForEReceipt() {
        System.out.print("Do you want to get an e-receipt? (Y/N): ");

        return scanner.nextLine().toLowerCase().equals("y");
    }

    private static MailAddress askForEMail() {
        MailAddress selectedMailAddress = null;
        while(selectedMailAddress == null) {
            System.out.print("Write down your e-mail adress: ");
            String name = scanner.nextLine();
            System.out.print("Are you sure " + name + " is your e-mail address? (Y/N): ");
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
