package services;

import data.DigitalSignature;
import data.Nif;
import data.Party;
import exceptions.data.NotValidDigitalSignatureException;
import exceptions.data.NotValidNifException;
import services.ElectoralOrganism;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;


public class ElectoralOrganismImplementation implements ElectoralOrganism {

    private Set<Nif> canVoteNifs;

    public ElectoralOrganismImplementation() {
        this.canVoteNifs = new HashSet<Nif>() {{
            try {
                add(new Nif("12345678A"));
                add(new Nif("B1234567B"));
                add(new Nif("22345678A"));
                add(new Nif("C1234567B"));
                add(new Nif("32345678A"));
                add(new Nif("D1234567B"));
                add(new Nif("42345678A"));
                add(new Nif("E1234567B"));
            } catch (NotValidNifException e) {
                e.printStackTrace();
            }
        }};
    }

    @Override
    public boolean canVote(Nif nif) {
        return canVoteNifs.contains(nif);
    }

    @Override
    public void disableVoter(Nif nif) {
        canVoteNifs.remove(nif);
    }

    @Override
    public DigitalSignature askForDigitalSignature(Party party) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(party.getName().getBytes(StandardCharsets.UTF_8));
            return new DigitalSignature(hash);
        } catch (NoSuchAlgorithmException | NotValidDigitalSignatureException e) {
            e.printStackTrace();
        }
        return null;
    }
}
