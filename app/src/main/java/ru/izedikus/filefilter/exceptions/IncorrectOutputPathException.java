package ru.izedikus.filefilter.exceptions;

/**
 * Thrown when the specified output directory non-writable.
 * Typically triggered during {@code ArgumentsMaker} finalization or path validation logic, so it must not appear.
 */
public class IncorrectOutputPathException extends RuntimeException {
    /**
     * Holds the invalid path that caused this exception.
     */
    final String Message;

    /**
     * Constructs the exception using the provided invalid path.
     *
     * @param path non-writable or invalid output directory
     */
    public IncorrectOutputPathException(String path) {
        this.Message = path;
    }

    /**
     * Returns the invalid path as the exception message.
     *
     * @return invalid output path
     */
    public String getMessage() {
        return this.Message;
    }
}
