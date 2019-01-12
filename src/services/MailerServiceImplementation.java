package services;

import data.DigitalSignature;
import data.MailAddress;
import services.MailerService;

public class MailerServiceImplementation implements MailerService {
    @Override
    public void send(MailAddress address, DigitalSignature signature) {
        System.out.println("A mail has been sent to " + address.getMail());
    }
}
