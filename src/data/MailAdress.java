package data;

public final class MailAdress {
    private final String mail;

    public MailAdress(String mail){
        this.mail = mail;
    }

    public boolean isValid() {
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
        MailAdress mailAdress1 = (MailAdress) o;
        return mail.equals(mailAdress1.mail);
    }

    @Override
    public int hashCode() {
        return mail.hashCode();
    }

    @Override
    public String toString() {
        return "MailAdress{" + "mail='" + mail + '\'' + '}';
    }
}
