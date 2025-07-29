package ru.izedikus.filefilter.input;

import ru.izedikus.filefilter.models.Arguments;

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
