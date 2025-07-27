package ru.izedikus.filefilter;

import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;

public class CLI {
  public static void main(String[] args) {
    Arguments parsedArgs = InputController.parse(args);
    Statistics stats = FileController.generateStatisticsAndOutputFiles(parsedArgs);
    OutputController.printStatistics(stats, parsedArgs.fullStatsFlag());
  }
}
