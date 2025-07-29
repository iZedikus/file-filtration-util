package ru.izedikus.filefilter.models;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Statistical summary for parsed input files.
 *
 * @param countInt     number of parsed integers
 * @param countFloat   number of parsed decimals
 * @param countString  number of parsed strings
 * @param sumInt       total sum of all parsed integers
 * @param minInt       minimal parsed integer
 * @param maxInt       maximal parsed integer
 * @param avgInt       arithmetic mean of parsed integers
 * @param sumFloat     total sum of all parsed decimals
 * @param minFloat     minimal parsed decimal
 * @param maxFloat     maximal parsed decimal
 * @param avgFloat     arithmetic mean of parsed decimals
 * @param minStringLen length of the shortest parsed string
 * @param maxStringLen length of the longest parsed string
 */
public record Statistics(
        Long countInt,
        Long countFloat,
        Long countString,

        BigInteger sumInt,
        BigInteger minInt,
        BigInteger maxInt,
        BigDecimal avgInt,

        BigDecimal sumFloat,
        BigDecimal minFloat,
        BigDecimal maxFloat,
        BigDecimal avgFloat,

        int minStringLen,
        int maxStringLen
) {
}
