package utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BigMaths {

    private static BigDecimal getPercentIncreaseBetween(BigInteger passport, BigInteger scanned) {
        BigDecimal decimalPassport = new BigDecimal(passport);
        BigDecimal decimalScanned = new BigDecimal(scanned);

        return decimalScanned.subtract(decimalPassport).abs().divide(decimalPassport, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }

    public static boolean areBigIntegersSimilar(BigInteger read, BigInteger scanned) {
        //System.out.println(BigMaths.getPercentIncreaseBetween(read, scanned).toString());
        return BigMaths.getPercentIncreaseBetween(read, scanned).compareTo(BigDecimal.ONE) < 1;
    }
}
