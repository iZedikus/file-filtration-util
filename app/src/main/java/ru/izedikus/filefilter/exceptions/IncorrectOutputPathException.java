package ru.izedikus.filefilter.exceptions;

public class IncorrectOutputPathException extends RuntimeException {
    final String Message;

    public IncorrectOutputPathException(String path) {
        this.Message = path;
    }

    public String getMessage() {
        return this.Message;
    }
}
