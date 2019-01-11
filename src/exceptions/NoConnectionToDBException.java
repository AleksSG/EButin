package exceptions;

public class NoConnectionToDBException extends Exception {
    public NoConnectionToDBException() {super("Unable to connect to DataBase");}
}
