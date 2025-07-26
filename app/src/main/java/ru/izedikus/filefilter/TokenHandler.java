package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface TokenHandler {
    void handleInteger(BigInteger value);
    void handleFloat(BigDecimal value);
    void handleString(String value);
    Statistics getStatistics();
}
