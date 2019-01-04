package data;

import exceptions.NotValidBiometricDataException;
import utils.DataTypeConverter;

import java.util.Arrays;

public final class BiometricData {

    private final byte[] biometricData;

    public BiometricData(BiometricFacial bioFacial, BiometricFingerPrint bioFingerPrint) throws NotValidBiometricDataException {
        if(bioFacial == null || bioFingerPrint == null)
            throw new NotValidBiometricDataException();

        biometricData = new byte[bioFacial.getBioFacial().length + bioFingerPrint.getBioFingerPrint().length];
        System.arraycopy(bioFacial.getBioFacial(), 0, biometricData, 0, bioFacial.getBioFacial().length);
        System.arraycopy(bioFingerPrint.getBioFingerPrint(), 0, biometricData, bioFacial.getBioFacial().length, bioFingerPrint.getBioFingerPrint().length);
    }

    public byte[] getBiometricData() {
        return biometricData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BiometricData biometricData1 = (BiometricData) o;
        return Arrays.equals(this.biometricData, biometricData1.biometricData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(biometricData);
    }

    @Override
    public String toString() {
        return "BiometricData{" + "biometricData='" + DataTypeConverter.bytesToHex(biometricData) + '\'' + '}';
    }
}
