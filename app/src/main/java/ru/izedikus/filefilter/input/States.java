package ru.izedikus.filefilter.input;

/**
 * Internal value-handling states used in {@link ArgumentsMaker}
 * to indicate context-sensitive value expectations.
 */
public enum States {
    DEFAULT,
    WAITING_OUTPUT_PATH,
    WAITING_PREFIX
}
