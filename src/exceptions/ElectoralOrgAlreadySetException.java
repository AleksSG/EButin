package exceptions;

public class ElectoralOrgAlreadySetException extends Exception {
    public ElectoralOrgAlreadySetException(){
        super("The Electoral Organism has already been set.");
    }
}
