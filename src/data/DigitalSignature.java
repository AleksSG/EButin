package data;

import utils.DataTypeConverter;

import java.util.Arrays;

public final class DigitalSignature {

    private final byte[] votingOption;

    public DigitalSignature(byte[] votingOption) throws  NullPointerException {
        if(votingOption == null)
            throw new NullPointerException("Digital Signature can't be null");

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
