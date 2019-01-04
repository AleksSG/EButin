import kiosk.VotingKiosk;
import verification.IdentityVerify;
import verification.ManualVerification;

public class Main {
    private VotingKiosk votingKiosk;
    private final IdentityVerify DEFAULT_VERIFICATION = new ManualVerification();
    private IdentityVerify verification;

    public static void main(String[] args) {
        System.out.println("Hello World");
        new Main().votingProcess();
    }

    public void votingProcess() {
        if (demanarConsentiment()) {
            //verification = new BiometricVerification();
        }
        verification = DEFAULT_VERIFICATION;
    }

    public boolean demanarConsentiment() {
        return true;
    }
}
