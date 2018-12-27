package services;
import data.MailAddress;
import data.DigitalSignature;

public interface MailerService {
    void send(MailAddress address, DigitalSignature signature);
}
