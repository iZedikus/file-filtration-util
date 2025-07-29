package ru.izedikus.filefilter.input;

import java.util.ArrayList;
import java.util.List;

import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.output.OutputMessage;

public class InputController {
    public static Arguments parse(String[] args) {
        ArgumentsMaker argsMaker = new ArgumentsMaker();

        for (String arg : args) {
            if (arg.charAt(0) == '-') argsMaker.handleFlag(arg);
            else argsMaker.handleValue(arg);
        }

        return argsMaker.generateArguments();
    }
}
