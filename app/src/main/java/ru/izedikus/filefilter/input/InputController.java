package ru.izedikus.filefilter.input;

import ru.izedikus.filefilter.exceptions.HelpRequestException;
import ru.izedikus.filefilter.models.Arguments;

/**
 * Parses CLI arguments and converts them into an {@link Arguments} object.
 */
public class InputController {
    /**
     * Processes command-line arguments and constructs an {@link Arguments} record
     * with validated values and flags.
     *
     * @param args raw command-line arguments
     * @return {@link Arguments} filled according to input arguments
     */
    public static Arguments parse(String[] args) throws HelpRequestException {
        ArgumentsMaker argsMaker = new ArgumentsMaker();

        for (String arg : args) {
            if (arg.charAt(0) == '-') argsMaker.handleFlag(arg);
            else argsMaker.handleValue(arg);
        }

        return argsMaker.generateArguments();
    }
}
