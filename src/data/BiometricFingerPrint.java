package data;

import exceptions.NotValidBiometricFingerPrintException;
import utils.DataTypeConverter;

import java.math.BigInteger;
import java.util.Arrays;

public final class BiometricFingerPrint {

    private final BigInteger bioFingerPrint;

    public BiometricFingerPrint(BigInteger bioFingerPrint) throws NotValidBiometricFingerPrintException {
        if(bioFingerPrint == null)
            throw new NotValidBiometricFingerPrintException();

        this.bioFingerPrint = bioFingerPrint;
    }

    public BigInteger getNumber() {
        return bioFingerPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricFingerPrint biometricFingerPrint = (BiometricFingerPrint) o;
        return bioFingerPrint.equals(biometricFingerPrint.bioFingerPrint);
    }

    @Override
    public int hashCode() {
        return bioFingerPrint.hashCode();
    }

    @Override
    public String toString() {
        return "BiometricFingerPrint{" + "bioFingerPrint='" + bioFingerPrint.toString() + '\'' + '}';
    }
}
