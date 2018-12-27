package kiosk;

import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.InvalidNifException;
import services.*;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;
    private Nif nif;
    private Party opcVot;

    public VotingKiosk(Nif nif) {
        elecOrg = null;
        mService = null;
        this.nif = nif;
    }

    public void setElectoralOrganism(ElectoralOrganism eO){
        this.elecOrg = eO;
    }

    public void setMailerService(MailerService mService){
        this.mService = mService;
    }

    public void vote(Party party) throws InvalidNifException {
        if (!elecOrg.canVote(nif)) {
            throw new InvalidNifException("This user can't vote.");
        }

        //scrutinize votecounter party
        opcVot = party;
    }

    public void sendeReceipt(MailAddress address) {
        mService.send(address, elecOrg.askForDigitalSignature(opcVot));
    }
}
