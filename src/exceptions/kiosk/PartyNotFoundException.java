package exceptions.kiosk;

public class PartyNotFoundException extends Exception {
    public PartyNotFoundException(){
        super("The given party wasn't found");
    }
}
