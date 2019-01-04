package data;

import exceptions.NotValidBiometricFacialException;
import utils.DataTypeConverter;

import java.util.Arrays;

public final class BiometricFacial {

    private final byte[] bioFacial;

    public BiometricFacial(byte[] bioFacial) throws NotValidBiometricFacialException {
        if(bioFacial == null || bioFacial.length != 32)
            throw new NotValidBiometricFacialException();

        this.bioFacial = bioFacial;
    }

    byte[] getBioFacial() {
        return bioFacial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricFacial biometricFacial = (BiometricFacial) o;
        return Arrays.equals(bioFacial, biometricFacial.bioFacial);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bioFacial);
    }

    @Override
    public String toString() {
        return "BiometricFacial{" + "bioFacial='" + DataTypeConverter.bytesToHex(bioFacial) + '\'' + '}';
    }
}
