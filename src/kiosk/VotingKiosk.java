package kiosk;

import data.MailAddress;
import data.Party;
import exceptions.*;
import exceptions.data.NotValidDigitalSignatureException;
import services.ElectoralOrganism;
import services.MailerService;
import services.PartiesDB;
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
        this.voteCounter = null;
    }

    public void setPartiesDB(PartiesDB partiesDB) throws NoConnectionToDBException, NotValidSetOfPartiesException {
        this.voteCounter = new VoteCounter(partiesDB.getPartiesFromDB());
    }

    public void setElectoralOrganism(ElectoralOrganism eO) {
        this.elecOrg = eO;
    }

    public void setMailerService(MailerService mService) {
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

    public Set<Party> getPartiesFromVoteCounter() {
        if(voteCounter != null)
            return voteCounter.getValidParties();
        return null;
    }

}
