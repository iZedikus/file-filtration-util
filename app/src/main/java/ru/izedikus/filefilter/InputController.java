package ru.izedikus.filefilter;

import java.util.ArrayList;
import java.util.List;
import ru.izedikus.filefilter.models.Arguments;

public class InputController {
  public static Arguments parse(String[] args) {
    List<String> inputFiles = new ArrayList<>();
    String outputPath = "";
    String prefix = "";
    boolean addFlag = false;
    boolean fullFlag = false;

    boolean isOutput = false;
    boolean isPrefix = false;

    for (String arg : args) {
      if (arg.charAt(0) == '-') {
        switch (arg) {
          case "-a", "--add":
            addFlag = true;
            break;
          case "-f", "--full":
            fullFlag = true;
            break;
          case "-o", "--output":
            isOutput = true;
            break;
          case "-p", "--prefix":
            isPrefix = true;
            break;
          default:
            break;
        }
        continue;
      }

      if (isOutput) {
        outputPath = arg;
        isOutput = false;
        continue;
      }

      if (isPrefix) {
        prefix = arg;
        isPrefix = false;
        continue;
      }

      inputFiles.add(arg);
    }

    return new Arguments(
        inputFiles,
        outputPath,
        prefix,
        addFlag,
        fullFlag);
  }
}
