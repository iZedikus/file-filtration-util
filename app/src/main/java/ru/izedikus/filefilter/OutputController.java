package ru.izedikus.filefilter;

import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.models.Statistics;

public class OutputController {
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
        System.out.println(OutputMessage.END);
    }

    public static void printFullStatistics(Statistics stats) {
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
    }

    public static void printMessage(OutputMessage message) {
        OutputController.printMessage(message, "");
    }

    public static void printMessage(OutputMessage message, String content) {
        System.out.println(message.getValue(content));
    }
}
