package ru.izedikus.filefilter.processing;

import ru.izedikus.filefilter.models.Statistics;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Strategy interface for handling classified tokens and producing a statistics summary.
 * Implementations define how each type is processed (e.g. counted vs fully-aggregated).
 */
public interface TokenHandler {
    /**
     * Handles a parsed integer.
     *
     * @param value token parsed as integer
     */
    void handleInteger(BigInteger value);

    /**
     * Handles a parsed floating-point.
     *
     * @param value token parsed as float
     */
    void handleFloat(BigDecimal value);

    /**
     * Handles a token classified as string.
     *
     * @param value token not matching numeric types
     */
    void handleString(String value);

    /**
     * Returns a {@link Statistics} object based on accumulated results.
     *
     * @return statistics summary
     */
    Statistics getStatistics();
}
