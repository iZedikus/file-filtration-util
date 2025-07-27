package ru.izedikus.filefilter;

import java.util.ArrayList;
import java.util.List;

import ru.izedikus.filefilter.models.Arguments;

public class InputController {
    public static Arguments parse(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        String outputPath = "";
        String prefix = "";
        Boolean addFlag = null;
        Boolean fullFlag = null;

        Boolean isOutput = null;
        Boolean isPrefix = null;

        for (String arg : args) {
            if (arg.charAt(0) == '-') {
                if (isOutput != null && isOutput) {
                    OutputController.printMessage(OutputMessage.NO_OUTPUT_PATH);
                    isOutput = false;
                }
                if (isPrefix != null && isPrefix) {
                    OutputController.printMessage(OutputMessage.NO_PREFIX);
                    isPrefix = false;
                }
                switch (arg) {
                    case "-a", "--add":
                        if (addFlag == null) {
                            addFlag = true;
                        } else {
                            OutputController.printMessage(OutputMessage.MANY_ADDS);
                        }
                        break;
                    case "-f", "--full":
                        if (fullFlag == null) {
                            fullFlag = true;
                        } else {
                            if (fullFlag) {
                                OutputController.printMessage(OutputMessage.MANY_FULLS);
                            } else {
                                OutputController.printMessage(OutputMessage.SHORT_AND_FULL);
                            }
                        }
                        break;
                    case "-s", "--short":
                        if (fullFlag == null) {
                            fullFlag = false;
                        } else {
                            if (!fullFlag) {
                                OutputController.printMessage(OutputMessage.MANY_SHORTS);
                            } else {
                                OutputController.printMessage(OutputMessage.SHORT_AND_FULL);
                            }
                        }
                        break;
                    case "-o", "--output":
                        if (isOutput == null) {
                            isOutput = true;
                        } else {
                            OutputController.printMessage(OutputMessage.MANY_OUTPUTS);
                        }
                        break;
                    case "-p", "--prefix":
                        if (isPrefix == null) {
                            isPrefix = true;
                        } else {
                            OutputController.printMessage(OutputMessage.MANY_PREFIXES);
                        }
                        break;
                    default:
                        OutputController.printMessage(OutputMessage.INCORRECT_FLAG);
                        break;
                }
                continue;
            }

            if (isOutput != null && isOutput) {
                if (arg.contains("|\\*:?\"<>")) {
                    OutputController.printMessage(OutputMessage.INCORRECT_OUTPUT_PATH);
                } else {
                    outputPath = arg;
                }
                isOutput = false;
                continue;
            }

            if (isPrefix != null && isPrefix) {
                if (arg.contains("/|\\*:?\"<>")) {
                    OutputController.printMessage(OutputMessage.INCORRECT_PREFIX);
                } else {
                    prefix = arg;
                }
                isPrefix = false;
                continue;
            }

            if (arg.contains("|\\*:?\"<>")) {
                OutputController.printMessage(OutputMessage.INCORRECT_TXT_FILE);
                continue;
            }
            if (!arg.endsWith(".txt")) {
                arg = arg + ".txt";
            }
            inputFiles.add(arg);
        }

        return new Arguments(
                inputFiles,
                outputPath,
                prefix,
                Boolean.TRUE.equals(addFlag),
                Boolean.TRUE.equals(fullFlag));
    }
}
