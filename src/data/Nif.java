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
}
