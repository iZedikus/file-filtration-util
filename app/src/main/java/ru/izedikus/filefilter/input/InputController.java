package ru.izedikus.filefilter.input;

import java.util.ArrayList;
import java.util.List;

import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.output.OutputMessage;

public class InputController {
    public static Arguments parse(String[] args) {
        List<String> inputFiles = new ArrayList<>();
        String outputPath = "";
        String prefix = "";
        Boolean addFlag = null;
        Boolean fullFlag = null;

        Boolean isOutput = null;
        Boolean isPrefix = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.charAt(0) == '-') {
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
                        if (isOutput != null) {
                            OutputController.printMessage(OutputMessage.MANY_OUTPUTS);
                        } else if (i == args.length -1 || args[i+1].charAt(0) == '-') {
                            OutputController.printMessage(OutputMessage.MISSING_OUTPUT_PATH);
                        } else {
                            isOutput = true;
                        }
                        break;
                    case "-p", "--prefix":
                        if (isPrefix != null) {
                            OutputController.printMessage(OutputMessage.MANY_PREFIXES);
                        } else if (i == args.length - 1 || args[i+1].charAt(0) == '-') {
                            OutputController.printMessage(OutputMessage.MISSING_PREFIX);
                        } else {
                            isPrefix = true;
                        }
                        break;
                    default:
                        OutputController.printMessage(OutputMessage.INCORRECT_FLAG, arg);
                        break;
                }
                continue;
            }

            if (isOutput != null && isOutput) {
                if (arg.matches(".*[|*:?\"<>].*")) {
                    OutputController.printMessage(OutputMessage.INCORRECT_OUTPUT_PATH);
                } else {
                    outputPath = arg.replace('\\', '/');
                    if (!outputPath.endsWith("/")) {
                        outputPath = outputPath + "/";
                    }
                }
                isOutput = false;
                continue;
            }

            if (isPrefix != null && isPrefix) {
                if (arg.matches(".*[/|*:?\"<>].*")) {
                    OutputController.printMessage(OutputMessage.INCORRECT_PREFIX);
                } else {
                    prefix = arg;
                }
                isPrefix = false;
                continue;
            }

            if (arg.matches(".*[|*:?\"<>].*")) {
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
