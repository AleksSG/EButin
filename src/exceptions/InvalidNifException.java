package exceptions;

public class InvalidNifException extends Exception {
    public InvalidNifException(){
        super("Your nif seems to be incorrect.");
    }
}
