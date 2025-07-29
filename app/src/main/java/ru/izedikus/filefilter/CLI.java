package ru.izedikus.filefilter;

import ru.izedikus.filefilter.exceptions.HelpRequestException;
import ru.izedikus.filefilter.exceptions.IncorrectOutputPathException;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.input.InputController;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.output.OutputController;
import ru.izedikus.filefilter.processing.FileController;

import static ru.izedikus.filefilter.output.OutputMessage.*;

/**
 * Entry point of the command-line utility for processing input files and generating statistics.
 * <p>
 * The application supports parsing various command-line flags and arguments to:
 * <ul>
 *     <li>Read specified input files with mixed-type data (integers, floats, strings);</li>
 *     <li>Generate statistical summaries based on the provided flags;</li>
 *     <li>Optionally save the processed data to an output directory with a specified prefix.</li>
 * </ul>
 *
 * <p>Expected flags include:
 * <ul>
 *     <li><code>-a</code> / <code>--add</code> – whether to add data to existing output files;</li>
 *     <li><code>-f</code> / <code>--full</code> – whether to print full statistics;</li>
 *     <li><code>-s</code> / <code>--short</code> – whether to print short statistics;</li>
 *     <li><code>-h</code> / <code>--help</code> - whether to abort program and print help message;</li>
 *     <li><code>-o &lt;path&gt;</code> / <code>--output &lt;path&gt;</code> – path to output directory;</li>
 *     <li><code>-p &lt;prefix&gt;</code> / <code>--prefix &lt;prefix&gt;</code> – file prefix for output files.</li>
 * </ul>
 *
 * <p>On invalid input (e.g., no input files or invalid output path), informative warnings will be printed to the console.
 */
public class CLI {
    /**
     * Parses CLI arguments, processes files, and outputs statistics.
     *
     * @param args command-line arguments provided by the user.
     */
    public static void main(String[] args) {
        try {
            Arguments parsedArgs = InputController.parse(args);
            Statistics stats = FileController.generateStatisticsAndOutputFiles(parsedArgs);
            OutputController.printStatistics(stats, parsedArgs.fullStatsFlag());
        } catch (ZeroInputFilesException e) {
            OutputController.printMessage(ZERO_INPUT_FILES_EXCEPTION);
        } catch (IncorrectOutputPathException e) {
            OutputController.printMessage(INCORRECT_OUTPUT_PATH_EXCEPTION, e.getMessage());
        } catch (HelpRequestException e) {
            OutputController.printMessage(HELP_MESSAGE);
        }
    }
}
