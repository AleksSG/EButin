package kiosk;

import data.MailAddress;
import data.Party;
import exceptions.data.NotValidDigitalSignatureException;
import exceptions.data.NotValidPartyException;
import exceptions.kiosk.*;
import exceptions.services.NoConnectionToDBException;
import exceptions.verification.VerificationIdentityFailedException;
import services.ElectoralOrganism;
import services.MailerService;
import services.PartiesDB;
import verification.IdentityVerify;

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

    public void closeSession() {
        this.session = null;
    }

    public void vote(Party party) throws NotValidPartyException, AServiceNotInitializedException, SessionNotStartedException, NotValidDigitalSignatureException, VotingRightsFailedException {
        if(party == null)
            throw new NotValidPartyException();

        if(elecOrg == null || voteCounter == null)
            throw new AServiceNotInitializedException();

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
        return new HashSet<>();
    }

}
