package ru.izedikus.filefilter;

import ru.izedikus.filefilter.exceptions.IncorrectOutputPathException;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.input.InputController;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.processing.FileController;

import static ru.izedikus.filefilter.output.OutputMessage.*;

public class CLI {
    public static void main(String[] args) {
        try {
            Arguments parsedArgs = InputController.parse(args);
            Statistics stats = FileController.generateStatisticsAndOutputFiles(parsedArgs);
            OutputController.printStatistics(stats, parsedArgs.fullStatsFlag());
        } catch (ZeroInputFilesException e) {
            OutputController.printMessage(ZERO_INPUT_FILES_EXCEPTION);
        } catch (IncorrectOutputPathException e) {
            OutputController.printMessage(INCORRECT_OUTPUT_PATH_EXCEPTION, e.getMessage());
        }
    }
}
