package ru.izedikus.filefilter.input;

import ru.izedikus.filefilter.exceptions.HelpRequestException;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.output.OutputMessage;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static ru.izedikus.filefilter.output.OutputMessage.*;

/**
 * Interprets raw CLI input and producing an {@link Arguments}.
 * Maintains internal state to handle flags and their values correctly.
 */
class ArgumentsMaker {
    List<String> inputFiles = new ArrayList<>();
    String outputPath = "";
    String prefix = "";
    Boolean addFlag = null;
    Boolean fullFlag = null;

    States state = States.DEFAULT;

    /**
     * Adds a CLI flag to the final arguments configuration or switches value-handling state accordingly.
     *
     * @param flag CLI flag (e.g., {@code -f}, {@code -o}, etc.)
     */
    public void handleFlag(String flag) {
        warnIfInWaitingState();
        switch (flag) {
            case "-a", "--add" -> handleAddFlag();
            case "-f", "--full" -> handleFullFlag();
            case "-s", "--short" -> handleShortFlag();
            case "-o", "--output" -> handleOutputFlag();
            case "-p", "--prefix" -> handlePrefixFlag();
            case "-h", "--help" -> throw new HelpRequestException();
            default -> OutputController.printMessage(INCORRECT_FLAG, flag);
        }
    }

    private void handleAddFlag() {
        if (addFlag == null) addFlag = true;
        else OutputController.printMessage(MANY_ADDS);
    }

    private void handleFullFlag() {
        if (fullFlag == null) fullFlag = true;
        else if (!fullFlag) OutputController.printMessage(SHORT_AND_FULL);
        else OutputController.printMessage(MANY_FULLS);
    }

    private void handleShortFlag() {
        if (fullFlag == null) fullFlag = false;
        else if (fullFlag) OutputController.printMessage(SHORT_AND_FULL);
        else OutputController.printMessage(MANY_SHORTS);
    }

    private void handleOutputFlag() {
        state = States.WAITING_OUTPUT_PATH;
    }

    private void handlePrefixFlag() {
        state = States.WAITING_PREFIX;
    }

    /**
     * Adds a value to the final arguments configuration depending on current value-handling state
     * (e.g., file path, output path, or prefix).
     *
     * @param value non-flag CLI argument
     */
    public void handleValue(String value) {
        value = value.replace('\\', '/');

        switch (state) {
            case DEFAULT -> handleFilePath(value);
            case WAITING_PREFIX -> handlePrefix(value);
            case WAITING_OUTPUT_PATH -> handleOutputPath(value);
        }
    }

    private void handleFilePath(String path) {
        path = path.endsWith(".txt") ? path : path + ".txt";

        if (!isInputFilePathValid(path)) OutputController.printMessage(INPUT_FILE_NOT_FOUND, path);
        else inputFiles.add(path);

        state = States.DEFAULT;
    }

    private boolean isInputFilePathValid(String path) {
        try {
            return Files.exists(Path.of(path));
        } catch (RuntimeException e) {
            return false;
        }
    }

    private void handleOutputPath(String path) {
        path = path.endsWith("/") ? path : path + "/";

        if (!outputPath.isEmpty()) OutputController.printMessage(MANY_OUTPUTS);
        else if (!isOutputPathValid(path)) OutputController.printMessage(INCORRECT_OUTPUT_PATH, path);
        else outputPath = path;

        state = States.DEFAULT;
    }

    private boolean isOutputPathValid(String path) {
        try {
            return Files.isWritable(Path.of(path));
        } catch (InvalidPathException e) {
            return false;
        }
    }

    private void handlePrefix(String _prefix) {
        if (!prefix.isEmpty()) OutputController.printMessage(MANY_PREFIXES);
        else if (!isPrefixValid(_prefix)) OutputController.printMessage(INCORRECT_PREFIX);
        else prefix = _prefix;

        state = States.DEFAULT;
    }

    private boolean isPrefixValid(String prefix) {
        return !prefix.matches(".*[<>:\"/\\\\|?*\\x00-\\x1F].*");
    }

    private void warnIfInWaitingState() {
        if (state == States.DEFAULT) return;

        switch (state) {
            case WAITING_OUTPUT_PATH:
                if (outputPath.isEmpty()) OutputController.printMessage(OutputMessage.MISSING_OUTPUT_PATH);
                break;
            case WAITING_PREFIX:
                if (prefix.isEmpty()) OutputController.printMessage(OutputMessage.MISSING_PREFIX);
                break;
        }
        state = States.DEFAULT;
    }

    /**
     * Finalizes and constructs the {@link Arguments} based on parsed data.
     * Will interpret null flags as {@code false}, and default strings as empty.
     *
     * @return fully built {@link Arguments}
     */
    public Arguments generateArguments() {
        warnIfInWaitingState();
        return new Arguments(
                inputFiles,
                outputPath,
                prefix,
                Boolean.TRUE.equals(addFlag),
                Boolean.TRUE.equals(fullFlag));
    }
}
