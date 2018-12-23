package exceptions;

public class InvalidMailException extends Exception {
    public InvalidMailException(){
        super("Your mail address seems to be incorrect.");
    }
}
