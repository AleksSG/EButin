package kiosk;

import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import exceptions.InvalidSetOfPartiesException;
import services.*;

import java.util.HashSet;
import java.util.Set;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;
    private Nif nifVotantActual;
    private VoteCounter voteCounter;
    private DigitalSignature digSignVote;

    public VotingKiosk() throws InvalidSetOfPartiesException {
        this.elecOrg = null;
        this.mService = null;
        Set<Party> partySet = fillSet();
        this.voteCounter = new VoteCounter(partySet);
    }
    public void setElectoralOrganism(ElectoralOrganism eO){
        this.elecOrg = eO;
    }
    public void setMailerService(MailerService mService){
        this.mService = mService;
    }
    public void setNif(Nif nif) {
        this.nifVotantActual = nif;
    }

    public void vote(Party party) {
        if (elecOrg.canVote(this.nifVotantActual)) {
            voteCounter.scrutinize(party);
        }
        elecOrg.disableVoter(nifVotantActual);
        digSignVote = elecOrg.askForDigitalSignature(party);
    }
    //Aqui el sistema rep l'email del usuari (en el cas de que aquest hagi seleccionat SI en la UI)
    public void sendeReceipt(MailAddress address) {
        mService.send(address, digSignVote);
    }

    private Set<Party> fillSet(){
        Set<Party> partySet = new HashSet<>();
        //get parties from database
        partySet.add(new Party("PP"));
        partySet.add(new Party("PSC"));
        partySet.add(new Party("Cs"));
        return partySet;
    }
}
