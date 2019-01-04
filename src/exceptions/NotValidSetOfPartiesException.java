package exceptions;

public class NotValidSetOfPartiesException extends Exception {
    public NotValidSetOfPartiesException(){
        super("The set of valid parties can't be null or empty");
    }
}
