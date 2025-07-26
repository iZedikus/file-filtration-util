package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Statistics;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class TokenProcessor implements TokenHandler {
    Long countInt = 0L;
    Long countFloat = 0L;
    Long countString = 0L;

    BigInteger sumInt = BigInteger.ZERO;
    BigInteger minInt = BigInteger.ZERO;
    BigInteger maxInt = BigInteger.ZERO;

    BigDecimal sumFloat = BigDecimal.ZERO;
    BigDecimal minFloat = BigDecimal.ZERO;
    BigDecimal maxFloat = BigDecimal.ZERO;

    int minStringLen = Integer.MAX_VALUE;
    int maxStringLen = 0;

    @Override
    public void handleInteger(BigInteger curInteger) {
        countInt++;
        sumInt = sumInt.add(curInteger);
        minInt = (curInteger.compareTo(minInt) < 0) ? curInteger : minInt;
        maxInt = (curInteger.compareTo(maxInt) > 0) ? curInteger : maxInt;
    }

    @Override
    public void handleFloat(BigDecimal curDecimal) {
        countFloat++;
        sumFloat = sumFloat.add(curDecimal);
        minFloat = (curDecimal.compareTo(minFloat) < 0) ? curDecimal : minFloat;
        maxFloat = (curDecimal.compareTo(maxFloat) > 0) ? curDecimal : maxFloat;
    }

    @Override
    public void handleString(String curString) {
        countString++;
        minStringLen = Math.min(minStringLen, curString.length());
        maxStringLen = Math.max(maxStringLen, curString.length());
    }

    @Override
    public Statistics getStatistics() {
        BigDecimal avgInt = countInt > 0
                ? new BigDecimal(sumInt).divide(BigDecimal.valueOf(countInt), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal avgFloat = countFloat > 0
                ? sumFloat.divide(BigDecimal.valueOf(countFloat), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new Statistics(
                countInt,
                countFloat,
                countString,
                sumInt,
                minInt,
                maxInt,
                avgInt,
                sumFloat,
                minFloat,
                maxFloat,
                avgFloat,
                minStringLen,
                maxStringLen
        );
    }
}