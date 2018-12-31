package exceptions;

public class SessionNotStartedException extends Exception {
    public SessionNotStartedException(){
        super("There is not current session active.");
    }
}
