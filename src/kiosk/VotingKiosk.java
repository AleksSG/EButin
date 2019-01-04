package kiosk;

import data.MailAddress;
import data.Party;
import exceptions.*;
import services.ElectoralOrganism;
import services.MailerService;
import verification.IdentityVerify;

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

    public void startSession(IdentityVerify identityVerify) throws SessionNotFinishedException, VerificationIdentityFailedException {
        if(session != null)
            throw new SessionNotFinishedException();

        session = new Session(identityVerify);
    }

    public void closeSession() throws SessionNotStartedException {
        if(this.session == null)
            throw new SessionNotStartedException();

        this.session = null;
    }

    public void vote(Party party) throws SessionNotStartedException, NotValidDigitalSignatureException, VotingRightsFailedException {
        if(party == null)
            throw new NullPointerException("The parameter party mustn't be null.");
        if(elecOrg == null)
            throw new NullPointerException("The ElectoralOrganism han't ben set yet.");
        if(voteCounter == null)
            throw new NullPointerException("The VoteCounter hasn't been initialized.");
        if(session == null)
            throw new SessionNotStartedException();

        if (!elecOrg.canVote(this.session.getNif()))
            throw new VotingRightsFailedException();

        voteCounter.scrutinize(party);
        elecOrg.disableVoter(this.session.getNif());
        session.setDigitalSignature(elecOrg.askForDigitalSignature(party));
    }

    public void sendeReceipt(MailAddress address) throws SessionNotStartedException, HasNotVotedException {
        if(session == null)
            throw new SessionNotStartedException();

        mService.send(address, this.session.getDigitalSignature());
    }
    
    public Set<Party> getPartiesFromDB(){
        return null;
    }

}
