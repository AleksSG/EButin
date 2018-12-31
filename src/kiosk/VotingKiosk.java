package kiosk;

import data.MailAddress;
import data.Nif;
import data.Party;
import exceptions.*;
import services.*;

import java.util.HashSet;
import java.util.Set;


public class VotingKiosk {

    private ElectoralOrganism elecOrg;
    private MailerService mService;

    private VoteCounter voteCounter;
    private Session session;

    public VotingKiosk() {
        this.elecOrg = null;
        this.mService = null;
        this.session = null;
        setValidParties();
    }

    private void setValidParties() {
        try {
            //get parties from an internal/external database
            //a default one has been put instead, modify getPartiesFromDB in order to add the one needed.
            this.voteCounter = new VoteCounter(getPartiesFromDB());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setElectoralOrganism(ElectoralOrganism eO) throws ElectoralOrgAlreadySetException {
        if(elecOrg != null)
            throw new ElectoralOrgAlreadySetException();

        this.elecOrg = eO;
    }

    public void setMailerService(MailerService mService) throws MailerServiceAlreadySetException {
        if(this.mService != null)
            throw new MailerServiceAlreadySetException();

        this.mService = mService;
    }

    public void startSession(Nif nif) throws SessionNotFinishedException {
        if(session != null)
            throw new SessionNotFinishedException();

        session = new Session(nif);
    }

    public void vote(Party party) throws SessionNotStartedException {
        if(party == null)
            throw new NullPointerException("The parameter party mustn't be null.");
        if(elecOrg == null)
            throw new NullPointerException("The ElectoralOrganism han't ben set yet.");
        if(voteCounter == null)
            throw new NullPointerException("The VoteCounter hasn't been initialized.");
        if(session == null)
            throw new SessionNotStartedException();

        if (elecOrg.canVote(this.session.getNif())) {
            voteCounter.scrutinize(party);
            elecOrg.disableVoter(this.session.getNif());
        }

        this.session.setDigitalSignature(elecOrg.askForDigitalSignature(party));
    }

    public void sendeReceipt(MailAddress address) throws SessionNotStartedException, HasNotVotedException {
        if(session == null)
            throw new SessionNotStartedException();

        mService.send(address, this.session.getDigitalSignature());
    }

    public void closeSession() {
        this.session = null;
    }
    
    public Set<Party> getPartiesFromDB(){
        return null;
    }

}
