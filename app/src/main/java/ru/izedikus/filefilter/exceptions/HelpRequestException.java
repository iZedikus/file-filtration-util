package ru.izedikus.filefilter.exceptions;

/**
 * Thrown when {@code --help} flag appears in arguments.
 * Typically triggered during {@code ArgumentsMaker.parse()} method, so program can gracefully stop itself.
 */
public class HelpRequestException extends RuntimeException {
}
