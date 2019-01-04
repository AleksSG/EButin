package data;

import exceptions.NotValidDigitalSignatureException;
import utils.DataTypeConverter;

import java.util.Arrays;

public final class DigitalSignature {

    private final byte[] votingOption;

    public DigitalSignature(byte[] votingOption) throws NotValidDigitalSignatureException {
        if(votingOption == null || votingOption.length != 32)
            throw new NotValidDigitalSignatureException();

        this.votingOption = votingOption;
    }

    public byte[] getVotingOption() {
        return votingOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DigitalSignature dataSignature1 = (DigitalSignature) o;
        return Arrays.equals(votingOption, dataSignature1.votingOption);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(votingOption);
    }

    @Override
    public String toString() {
        return "DataSignature{" + "votingOption='" + DataTypeConverter.bytesToHex(votingOption) + '\'' + '}';
    }
}
