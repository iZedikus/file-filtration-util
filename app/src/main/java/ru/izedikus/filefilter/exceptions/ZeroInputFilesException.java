package ru.izedikus.filefilter.exceptions;

/**
 * Thrown when no valid input files were provided for processing.
 * Typically, indicates that the CLI input did not find any usable .txt files.
 */
public class ZeroInputFilesException extends RuntimeException {
}
