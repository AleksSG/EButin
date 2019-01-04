package exceptions;

public class MailerServiceAlreadySetException extends Exception {
    public MailerServiceAlreadySetException(){
        super("The Mailer Service has already been set.");
    }
}
