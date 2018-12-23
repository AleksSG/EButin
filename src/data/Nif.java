package data;

public final class Nif {
    private final String nif;

    public Nif(String nif){
        this.nif = nif;
    }

    public boolean isValid() {
        return isValidNif() || isValidNie();
    }

    private boolean isValidNif() {
        return this.nif.matches("[0-9]{8}[A-HJ-NP-TV-Z]");
    }

    private boolean isValidNie() {
        return this.nif.matches("[A-HJ-NP-TV-Z][0-9]{7}[A-HJ-NP-TV-Z]");
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
