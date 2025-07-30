package ru.izedikus.filefilter.output;

import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.models.Statistics;

/**
 * Manages all program output.
 * Prints informational messages, processing results or warnings.
 */
public class OutputController {
    /**
     * Prints either short or full statistics summary, based on the {@code full} flag.
     * Throws {@link ZeroInputFilesException} if no input data was processed.
     *
     * @param stats statistical summary object
     * @param full  whether to print full statistics instead of short
     */
    public static void printStatistics(Statistics stats, boolean full) {
        if (stats.countInt() == 0 && stats.countFloat() == 0 && stats.countString() == 0) {
            throw new ZeroInputFilesException();
        }
        System.out.println(OutputMessage.SUCCESS.getValue());
        if (full) {
            printFullStatistics(stats);
            return;
        }
        System.out.println(OutputMessage.SHORT_STATS.getValue());
        if (stats.countInt() != 0) {
            System.out.println(OutputMessage.SHORT_STATS_INT.getValue(stats.countInt()));
        }
        if (stats.countFloat() != 0) {
            System.out.println(OutputMessage.SHORT_STATS_FLOAT.getValue(stats.countFloat()));
        }
        if (stats.countString() != 0) {
            System.out.println(OutputMessage.SHORT_STATS_STRING.getValue(stats.countString()));
        }
        System.out.println(OutputMessage.END.getValue());
    }

    /**
     * Prints full statistics: counts, sums, min, max and average values
     * for each data type — integers, floats, and strings.
     *
     * @param stats statistical summary object
     */
    static void printFullStatistics(Statistics stats) {
        System.out.println(OutputMessage.FULL_STATS.getValue());
        if (stats.countInt() != 0) {
            System.out.println(OutputMessage.FULL_STATS_INT.getValue(
                    stats.countInt(),
                    stats.sumInt(),
                    stats.minInt(),
                    stats.maxInt(),
                    stats.avgInt()
            ));
        }
        if (stats.countFloat() != 0) {
            System.out.println(OutputMessage.FULL_STATS_FLOAT.getValue(
                    stats.countFloat(),
                    stats.sumFloat(),
                    stats.minFloat(),
                    stats.maxFloat(),
                    stats.avgFloat()
            ));
        }
        if (stats.countString() != 0) {
            System.out.println(OutputMessage.FULL_STATS_STRING.getValue(
                    stats.countString(),
                    stats.minStringLen(),
                    stats.maxStringLen()
            ));
        }
        System.out.println(OutputMessage.END.getValue());
    }

    /**
     * Prints a message without additional content.
     *
     * @param message {@link OutputMessage} to print
     */
    public static void printMessage(OutputMessage message) {
        OutputController.printMessage(message, "");
    }

    /**
     * Prints a message with optional formatted content.
     *
     * @param message {@link OutputMessage} to print
     * @param content optional formatting argument for the message
     */
    public static void printMessage(OutputMessage message, String content) {
        System.out.println(message.getValue(content));
    }
}
