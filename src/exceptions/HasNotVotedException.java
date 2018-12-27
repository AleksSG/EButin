package exceptions;

public class HasNotVotedException extends Exception {
    public HasNotVotedException(){
        super("User hasn't voted yet.");
    }
}
