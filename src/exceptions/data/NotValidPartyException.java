package exceptions.data;

public class NotValidPartyException extends Exception {
    public NotValidPartyException(){
        super("Your party name seems to be incorrect.");
    }
}
