package ru.izedikus.filefilter.models;

import java.math.BigDecimal;
import java.math.BigInteger;

public record Statistics(
  Long countInt,
  Long countFloat,
  Long countString,

  BigInteger sumInt,
  BigInteger minInt,
  BigInteger maxInt,
  BigInteger avgInt,

  BigDecimal sumFloat,
  BigDecimal minFloat,
  BigDecimal maxFloat,
  BigDecimal avgFloat,

  Long minStringLen,
  Long maxStringLen
  ) {}
