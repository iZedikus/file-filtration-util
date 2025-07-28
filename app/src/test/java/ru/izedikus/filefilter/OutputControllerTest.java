package ru.izedikus.filefilter;

import org.junit.jupiter.api.Test;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.output.OutputController;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class OutputControllerTest {
    @Test
    public void printStatistics_shortDefault() {
        Statistics stats = new Statistics(
                1L, 1L, 1L,
                null, null, null, null,
                null, null, null, null,
                1, 1
        );

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        OutputController.printStatistics(stats, false);

        String output = outContent.toString();
        assertTrue(output.contains("Файлы успешно были обработаны!"));
        assertTrue(output.contains("КРАТКАЯ СТАТИСТИКА:"));
        assertTrue(output.contains("Целых чисел обнаружено: 1"));
        assertTrue(output.contains("Вещественных чисел обнаружено: 1"));
        assertTrue(output.contains("Строк обнаружено: 1"));
        assertTrue(output.contains("Программа завершает работу"));

        System.setOut(System.out);
    }

    @Test
    public void printStatistics_fullDefault() {
        Statistics stats = new Statistics(
                1L, 1L, 1L,
                BigInteger.ONE, BigInteger.ONE, BigInteger.ONE, BigDecimal.ONE,
                BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE,
                1, 1
        );

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        OutputController.printStatistics(stats, true);

        String output = outContent.toString();
        assertTrue(output.contains("Файлы успешно были обработаны!"));
        assertTrue(output.contains("ПОЛНАЯ СТАТИСТИКА:"));
        assertTrue(output.contains("Целых чисел обнаружено: 1"));
        assertTrue(output.contains("Сумма обнаруженных целых чисел: 1"));
        assertTrue(output.contains("Минимальное обнаруженное целое число: 1"));
        assertTrue(output.contains("Максимальное обнаруженное целое число: 1"));
        assertTrue(output.contains("Среднее значение обнаруженных целых чисел: 1"));
        assertTrue(output.contains("Вещественных чисел обнаружено: 1"));
        assertTrue(output.contains("Сумма обнаруженных вещественных чисел: 1"));
        assertTrue(output.contains("Минимальное обнаруженное вещественное число: 1"));
        assertTrue(output.contains("Максимальное обнаруженное вещественное число: 1"));
        assertTrue(output.contains("Среднее значение обнаруженных вещественных чисел: 1"));
        assertTrue(output.contains("Строк обнаружено: 1"));
        assertTrue(output.contains("Строка минимальной длины: 1"));
        assertTrue(output.contains("Строка максимальной длины: 1"));
        assertTrue(output.contains("Программа завершает работу"));

        System.setOut(System.out);
    }

    @Test
    public void printStatistics_ZeroFilesException() {
        Statistics stats = new Statistics(
                0L, 0L, 0L,
                null, null, null, null,
                null, null, null, null,
                0, 0
        );

        assertThrows(ZeroInputFilesException.class, () -> OutputController.printStatistics(stats, false));

        System.setOut(System.out);
    }
}
