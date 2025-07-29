package ru.izedikus.filefilter.models;

import java.util.List;

/**
 * CLI's behave arguments.
 *
 * @param inputFiles              list of input files' names with their
 *                                extensions (.txt)
 * @param outputPathIfBeenFlagged catalogue's path for placing output files.
 *                                {@code Empty} if {@code -o} flag was not used
 * @param prefixIfBeenFlagged     prefix for output files. {@code Empty} if
 *                                {@code -p} flag was not used
 * @param addFlag                 flag for adding text to files without
 *                                recreating them
 * @param fullStatsFlag           flag for turning full statistics mode.
 *                                {@code True} if {@code -f} flag was used, else
 *                                {@code False}
 */
public record Arguments(
        List<String> inputFiles,

        String outputPathIfBeenFlagged,
        String prefixIfBeenFlagged,
        boolean addFlag,
        boolean fullStatsFlag) {
}
