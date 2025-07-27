package ru.izedikus.filefilter;

import ru.izedikus.filefilter.exceptions.IncorrectOutputPathException;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;

public class CLI {
    public static void main(String[] args) {
        try {
            Arguments parsedArgs = InputController.parse(args);
            Statistics stats = FileController.generateStatisticsAndOutputFiles(parsedArgs);
            OutputController.printStatistics(stats, parsedArgs.fullStatsFlag());
        } catch (ZeroInputFilesException e) {
            OutputController.printMessage(OutputMessage.ZERO_INPUT_FILES);
        } catch (IncorrectOutputPathException e) {
            OutputController.printMessage(OutputMessage.INCORRECT_OUTPUT_PATH_EXCEPTION, e.getMessage());
        }
    }
}
