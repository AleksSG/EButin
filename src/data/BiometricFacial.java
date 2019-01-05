package data;

import exceptions.NotValidBiometricFacialException;
import utils.DataTypeConverter;

import java.math.BigInteger;
import java.util.Arrays;

public final class BiometricFacial {

    private final BigInteger bioFacial;

    public BiometricFacial(BigInteger bioFacial) throws NotValidBiometricFacialException {
        if(bioFacial == null)
            throw new NotValidBiometricFacialException();

        this.bioFacial = bioFacial;
    }

    public BigInteger getNumber() {
        return bioFacial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricFacial biometricFacial = (BiometricFacial) o;
        return bioFacial.equals(biometricFacial.bioFacial);
    }

    @Override
    public int hashCode() {
        return bioFacial.hashCode();
    }

    @Override
    public String toString() {
        return "BiometricFacial{" + "bioFacial='" + bioFacial.toString() + '\'' + '}';
    }
}
