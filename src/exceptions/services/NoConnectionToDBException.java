package exceptions.services;

public class NoConnectionToDBException extends Exception {
    public NoConnectionToDBException() {super("Unable to connect to DataBase");}
}
