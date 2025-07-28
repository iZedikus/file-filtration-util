package ru.izedikus.filefilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.processing.TokenFullStatsProcessor;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class TokenFullStatsProcessorTest {
    private TokenFullStatsProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new TokenFullStatsProcessor();
    }

    @Test
    void emptyStatistics() {
        Statistics expected = new Statistics(
                0L, 0L, 0L,
                BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                Integer.MAX_VALUE, 0
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
                new BigInteger("350"), new BigInteger("50"), new BigInteger("200"), new BigDecimal("116.67"),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                Integer.MAX_VALUE, 0
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
                new BigDecimal("36.50"), new BigDecimal("5.75"), new BigDecimal("20.25"), new BigDecimal("12.17"),
                Integer.MAX_VALUE, 0
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
                5, 23
        );

        assertEquals(expected, processor.getStatistics());
    }

    @Test
    void singleValueHandling() {
        processor.handleInteger(new BigInteger("42"));

        processor.handleFloat(new BigDecimal("3.14"));

        processor.handleString("test");

        Statistics expected = new Statistics(
                1L, 1L, 1L,
                new BigInteger("42"), new BigInteger("42"), new BigInteger("42"), new BigDecimal("42.00"),
                new BigDecimal("3.14"), new BigDecimal("3.14"), new BigDecimal("3.14"), new BigDecimal("3.14"),
                4, 4
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
                new BigInteger("300"), new BigInteger("100"), new BigInteger("200"), new BigDecimal("150.00"),
                new BigDecimal("30.75"), new BigDecimal("10.5"), new BigDecimal("20.25"), new BigDecimal("15.38"),
                4, 11
        );

        assertEquals(expected, processor.getStatistics());
    }
}
