package data;

import exceptions.NotValidBiometricFingerPrintException;
import utils.DataTypeConverter;

import java.util.Arrays;

public final class BiometricFingerPrint {

    private final byte[] bioFingerPrint;

    public BiometricFingerPrint(byte[] bioFingerPrint) throws NotValidBiometricFingerPrintException {
        if(bioFingerPrint == null || bioFingerPrint.length != 32)
            throw new NotValidBiometricFingerPrintException();

        this.bioFingerPrint = bioFingerPrint;
    }

    public byte[] getBioFingerPrint() {
        return bioFingerPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricFingerPrint biometricFingerPrint = (BiometricFingerPrint) o;
        return Arrays.equals(bioFingerPrint, biometricFingerPrint.bioFingerPrint);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bioFingerPrint);
    }

    @Override
    public String toString() {
        return "BiometricFingerPrint{" + "bioFingerPrint='" + DataTypeConverter.bytesToHex(bioFingerPrint) + '\'' + '}';
    }
}
