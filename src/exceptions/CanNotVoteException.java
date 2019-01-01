package exceptions;

public class CanNotVoteException extends Exception {
    public CanNotVoteException(){
        super("The Electoral Organism said you can't vote.");
    }
}
