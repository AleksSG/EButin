package kiosk;

import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import services.*;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;

    public VotingKiosk() {
        this.elecOrg = null;
        this.mService = null;
    }
    public void setElectoralOrganism(ElectoralOrganism eO){
        this.elecOrg = eO;
    }
    public void setMailerService(MailerService mService){
        this.mService = mService;
    }
    public void vote(Party party) throws InvalidNifException {
        //Nif nif = getNifVotant();
        Nif nif = new Nif("12345678A");
        if (elecOrg.canVote(nif)) {

        }
    }
    public void sendeReceipt(MailAddress address) {

    }
}
