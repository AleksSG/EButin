package kiosk;

import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import exceptions.InvalidSetOfPartiesException;
import exceptions.PartyNotFoundException;
import services.*;

import java.util.HashSet;
import java.util.Set;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;
    private Nif nif_votant_actual;
    private VoteCounter voteCounter;

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
        this.nif_votant_actual = nif;
    }
    public void vote(Party party) throws InvalidNifException {
        if (elecOrg.canVote(this.nif_votant_actual)) {
            voteCounter.scrutinize(party);
        }

        if (quiereRecibo()) {
            
        }
    }
    public void sendeReceipt(MailAddress address) {

    }

    private boolean quiereRecibo() {
        return true;
    }

    public void introducirEmailReciboE(MailAddress email) {

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
