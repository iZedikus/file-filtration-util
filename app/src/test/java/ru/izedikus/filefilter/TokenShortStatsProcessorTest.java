package ru.izedikus.filefilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.processing.TokenShortStatsProcessor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TokenShortStatsProcessorTest {
    private TokenShortStatsProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TokenShortStatsProcessor();
    }

    @Test
    void emptyStatistics() {
        Statistics expected = new Statistics(
                0L, 0L, 0L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void handleInteger() {
        processor.handleInteger(new BigInteger("100"));
        processor.handleInteger(new BigInteger("200"));
        processor.handleInteger(new BigInteger("50"));

        Statistics expected = new Statistics(
                3L, 0L, 0L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void handleFloat() {
        processor.handleFloat(new BigDecimal("10.5"));
        processor.handleFloat(new BigDecimal("20.25"));
        processor.handleFloat(new BigDecimal("5.75"));

        Statistics expected = new Statistics(
                0L, 3L, 0L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void handleString() {
        processor.handleString("short");
        processor.handleString("medium length");
        processor.handleString("very long string indeed");

        Statistics expected = new Statistics(
                0L, 0L, 3L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void mixedHandling() {
        // Integers
        processor.handleInteger(new BigInteger("100"));
        processor.handleInteger(new BigInteger("200"));

        // Floats
        processor.handleFloat(new BigDecimal("10.5"));
        processor.handleFloat(new BigDecimal("20.25"));

        // Strings
        processor.handleString("test");
        processor.handleString("longer test");

        Statistics expected = new Statistics(
                2L, 2L, 2L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void singleValueHandling() {
        // Single integer
        processor.handleInteger(new BigInteger("42"));

        // Single float
        processor.handleFloat(new BigDecimal("3.14"));

        // Single string
        processor.handleString("test");

        Statistics expected = new Statistics(
                1L, 1L, 1L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0
        );

        assertEquals(expected, processor.getStatistics());
    }
}