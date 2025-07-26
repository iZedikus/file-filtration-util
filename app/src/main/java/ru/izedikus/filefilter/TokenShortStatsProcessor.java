package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TokenShortStatsProcessor implements TokenHandler {
    Long countInt = 0L;
    Long countFloat = 0L;
    Long countString = 0L;

    @Override
    public void handleInteger(BigInteger curInteger) {
        countInt++;
    }

    @Override
    public void handleFloat(BigDecimal curDecimal) {
        countFloat++;
    }

    @Override
    public void handleString(String curString) {
        countString++;
    }

    @Override
    public Statistics getStatistics() {
        return new Statistics(
                countInt,
                countFloat,
                countString,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                0
        );
    }
}
