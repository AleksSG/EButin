package exceptions;

public class NotValidMailException extends Exception {
    public NotValidMailException(){
        super("Your mail address seems to be incorrect.");
    }
}
