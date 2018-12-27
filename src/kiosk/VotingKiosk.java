package kiosk;

import data.DigitalSignature;
import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.HasNotVotedException;
import exceptions.InvalidNifException;
import services.*;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;
    private Nif nif;
    private Party opcVot;

    public VotingKiosk(Nif nif) throws InvalidNifException {
        if(nif == null)
            throw new InvalidNifException("Nif can't be null");

        this.nif = nif;
        elecOrg = null;
        mService = null;
    }

    public void setElectoralOrganism(ElectoralOrganism eO){
        this.elecOrg = eO;
    }

    public void setMailerService(MailerService mService){
        this.mService = mService;
    }

    public void vote(Party party) throws InvalidNifException, NullPointerException {

        if(party == null)
            throw new NullPointerException("Party can't be null.");

        if (!elecOrg.canVote(nif))
            throw new InvalidNifException("This user can't vote.");

        opcVot = party;
        //scrutinize votecounter party

    }

    public void sendeReceipt(MailAddress address) throws HasNotVotedException {
        if(opcVot == null)
            throw new HasNotVotedException();

        mService.send(address, elecOrg.askForDigitalSignature(opcVot));
    }
}
