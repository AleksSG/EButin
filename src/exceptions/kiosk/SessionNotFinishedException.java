package exceptions.kiosk;

public class SessionNotFinishedException extends Exception {
    public SessionNotFinishedException(){
        super("Current session hasn't finished yet, there can't more than 1 concurrent session active.");
    }
}
