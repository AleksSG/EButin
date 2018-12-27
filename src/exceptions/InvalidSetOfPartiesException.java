package exceptions;

public class InvalidSetOfPartiesException extends Exception {
    public InvalidSetOfPartiesException(){
        super("The set of valid parties can't be null or empty");
    }
}
