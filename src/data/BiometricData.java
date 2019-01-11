package data;

import exceptions.data.NotValidBiometricDataException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public final class BiometricData {

    private final BigInteger bioFacial;
    private final BigInteger bioFingerPrint;

    public BiometricData(BigInteger bioFacial, BigInteger bioFingerPrint) throws NotValidBiometricDataException {
        if(bioFacial == null || bioFingerPrint == null)
            throw new NotValidBiometricDataException();

        this.bioFacial = bioFacial;
        this.bioFingerPrint = bioFingerPrint;
    }

    public boolean isNotSimilarTo(BiometricData biometricData) {
        return areNotBigIntegersSimilar(bioFacial, biometricData.bioFacial) || areNotBigIntegersSimilar(bioFingerPrint, biometricData.bioFingerPrint);
    }

    private static BigDecimal getPercentIncreaseBetween(BigInteger from, BigInteger value) {
        BigDecimal decimalPassport = new BigDecimal(from);
        BigDecimal decimalScanned = new BigDecimal(value);

        return decimalScanned.subtract(decimalPassport).abs().divide(decimalPassport, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }

    private static boolean areNotBigIntegersSimilar(BigInteger from, BigInteger value) {
        return getPercentIncreaseBetween(from, value).compareTo(BigDecimal.ONE) >= 1;
    }

    public BigInteger getBioFacial() {
        return bioFacial;
    }

    public BigInteger getBioFingerPrint() {
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
