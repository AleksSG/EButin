package data;

import exceptions.InvalidMailException;

public final class MailAddress {
    private final String mail;

    public MailAddress(String mail) throws InvalidMailException {
        if(mail == null || !isMailValid(mail))
            throw new InvalidMailException();

        this.mail = mail;
    }

    private static boolean isMailValid(String mail) {
        return mail.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
    }

    public String getMail() {
        return mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MailAddress mailAddress1 = (MailAddress) o;
        return mail.equals(mailAddress1.mail);
    }

    @Override
    public int hashCode() {
        return mail.hashCode();
    }

    @Override
    public String toString() {
        return "MailAddress{" + "mail='" + mail + '\'' + '}';
    }
}
