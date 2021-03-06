package data;

import exceptions.data.NotValidNifException;

public final class Nif {
    private final String nif;

    public Nif(String nif) throws NotValidNifException {
        if(nif == null || !isValidNif(nif))
            throw new NotValidNifException("Your nif seems to be incorrect.");

        this.nif = nif;
    }

    private static boolean isValidNif(String nif) {
        return isValidDni(nif) || isValidNie(nif);
    }

    private static boolean isValidDni(String nif) {
        return nif.matches("[0-9]{8}[A-HJ-NP-TV-Z]");
    }

    private static boolean isValidNie(String nif) {
        return nif.matches("[A-HJ-NP-TV-Z][0-9]{7}[A-HJ-NP-TV-Z]");
    }

    public String getNif() {
        return nif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Nif nif1 = (Nif) o;
        return nif.equals(nif1.nif);
    }

    @Override
    public int hashCode() {
        return nif.hashCode();
    }

    @Override
    public String toString() {
        return "Nif{" + "nif='" + nif + '\'' + '}';
    }
}
