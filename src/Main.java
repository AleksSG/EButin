import biometric.BiometricReader;
import data.*;
import exceptions.*;
import kiosk.VotingKiosk;
import services.ElectoralOrganism;
import verification.BiometricVerification;
import verification.IdentityVerify;
import verification.ManualVerification;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Main {
    private static final int VALID_STAFF_ID = 23;
    private static final String VALID_STAFF_PASSWORD = "root";

    private static Scanner reader;
    private static Set<Party> validParties;
    private static VotingKiosk votingKiosk;

    public static void main(String[] args) {

        //Initialize the reader from system input
        reader = new Scanner(System.in);

        //Initialize the set of valid parties
        validParties = new HashSet<Party>() {{
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

        //Initialize VotingKiosk instance
        votingKiosk = new VotingKiosk(){
            public Set<Party> getPartiesFromDB(){
                return validParties;
            }
        };

        try {
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
        } catch (ElectoralOrgAlreadySetException e) {
            e.printStackTrace();
            return;
        }

        try {
            votingKiosk.setMailerService((address, signature) -> System.out.println("A mail has been sent to " + address.getMail()));
        } catch (MailerServiceAlreadySetException e) {
            e.printStackTrace();
            return;
        }

        System.out.print("Write 'start' in order to start a voting process: ");
        while(reader.nextLine().equals("start")) {
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

        return reader.nextLine().toLowerCase().equals("y");
    }

    private static ManualVerification getManualVerificationProcess() {
        return new ManualVerification() {
            @Override
            public boolean logInSupportStaff() {
                System.out.println("A support staff member will verify your identity.");
                System.out.println("Please, wait until he or she comes.");
                reader.nextLine();
                System.out.print("[userId]: ");
                int staffMemberId = reader.nextInt();
                reader.nextLine();
                System.out.print("[password]: ");
                String staffMemberPassword = reader.nextLine();

                return staffMemberId == VALID_STAFF_ID && staffMemberPassword.equals(VALID_STAFF_PASSWORD);
            }

            @Override
            public Nif getManualNif() throws NotValidNifException {
                System.out.println("Write down the NIF verified: ");
                return new Nif(reader.nextLine());
            }
        };
    }

    private static BiometricVerification getBiometricVerificationProcess() {
        return new BiometricVerification(() -> {
            BiometricData dataScanned;
            BiometricData dataRead;

            try {
                System.out.println("We are going to start by scanning your face, put your face near the camera.");
                BiometricFacial faceScanned = new BiometricFacial(new byte[32]);
                Thread.sleep(3000);
                System.out.println("Great, now we are going to scan your fingerprints, place your index finger from right hand.");
                BiometricFingerPrint fingerPrintScanned = new BiometricFingerPrint(new byte[32]);
                dataScanned = new BiometricData(faceScanned, fingerPrintScanned);
                Thread.sleep(3000);
                System.out.println("Great! Please, enter your passport.");
                dataRead = ((BiometricReader) () -> new BiometricData(new BiometricFacial(new byte[32]), new BiometricFingerPrint(new byte[32]))).readBiometricData();
                Thread.sleep(3000);
                System.out.println("Your BiometricData has been sucessfully read from your passport.");
            } catch (NotValidBiometricFingerPrintException | NotValidBiometricFacialException | NotValidBiometricDataException | InterruptedException e) {
                e.printStackTrace();
                return;
            }

            if (!dataRead.equals(dataScanned))
                throw new BiometricVerificationFailedException();

            System.out.println("Everything is OK.");

        }) {
            @Override
            public Nif getNifFromPassport() throws NotValidNifException {
                return new Nif("12345678A");
            }
        };
    }

    private static void displayValidParties() {
        System.out.println("List of Valid Parties: ");
        validParties.forEach(v -> System.out.println("\t" + v.getName()));
        System.out.println();
    }

    private static Party askForAPartyToVote() {
        Party selectedParty = null;
        while(selectedParty == null) {
            System.out.print("Write down the name of the party you want to vote or press ENTER key to send a protest vote (is case sensitive): ");
            String name = reader.nextLine();
            System.out.print("Are you sure you want to vote " + name + "? (Y/N): ");
            if(reader.nextLine().toLowerCase().equals("y")) {
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

        return reader.nextLine().toLowerCase().equals("y");
    }

    private static MailAddress askForEMail() {
        MailAddress selectedMailAddress = null;
        while(selectedMailAddress == null) {
            System.out.print("Write down your e-mail adress: ");
            String name = reader.nextLine();
            System.out.print("Are you sure " + name + " is your e-mail address? (Y/N): ");
            if(reader.nextLine().toLowerCase().equals("y")) {
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
