package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Arguments;

public class CLI {
  public static void main(String[] args) {
    Arguments parsedArgs = InputController.parse(args);
  }
}
