package exceptions;

public class AServiceNotInitializedException extends Exception{
    public AServiceNotInitializedException(){
        super("A required service hasn't been initialized.");
    }
}
