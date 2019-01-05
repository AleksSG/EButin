package data;

import exceptions.NotValidBiometricDataException;
import utils.DataTypeConverter;

import java.util.Arrays;

public final class BiometricData {

    private final BiometricFacial bioFacial;
    private final BiometricFingerPrint bioFingerPrint;

    public BiometricData(BiometricFacial bioFacial, BiometricFingerPrint bioFingerPrint) throws NotValidBiometricDataException {
        if(bioFacial == null || bioFingerPrint == null)
            throw new NotValidBiometricDataException();

        this.bioFacial = bioFacial;
        this.bioFingerPrint = bioFingerPrint;
    }

    public BiometricFacial getBioFacial() {
        return bioFacial;
    }

    public BiometricFingerPrint getBioFingerPrint() {
        return bioFingerPrint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricData biometricData1 = (BiometricData) o;
        return biometricData1.bioFacial.equals(bioFacial) && biometricData1.bioFingerPrint.equals(bioFingerPrint);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 31 + bioFacial.hashCode();
        hash = hash * 31 + bioFingerPrint.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "BiometricData{" + "bioFacial='" + bioFacial.toString() + "', bioFingerPrint='" + bioFingerPrint.toString() + "'}";
    }
}
