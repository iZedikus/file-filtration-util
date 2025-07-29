package ru.izedikus.filefilter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.izedikus.filefilter.exceptions.IncorrectOutputPathException;
import ru.izedikus.filefilter.exceptions.ZeroInputFilesException;
import ru.izedikus.filefilter.input.InputController;
import ru.izedikus.filefilter.models.Arguments;
import ru.izedikus.filefilter.models.Statistics;
import ru.izedikus.filefilter.output.OutputMessage;
import ru.izedikus.filefilter.processing.FileController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileControllerTest {

    @TempDir
    Path tempDir;

    @Test
    void generateStatisticsAndOutputFiles_shouldCreateFilesAndReturnStats() throws IOException {
        Path inputFile = tempDir.resolve("file1.txt");
        Files.write(inputFile, List.of("123", "1.528535047E-25", "Персик"));

        String[] systemArgs = String.format(
                "%s -o %s",
                inputFile.toAbsolutePath(),
                tempDir.toAbsolutePath()
        ).split(" ");

        Arguments args = InputController.parse(systemArgs);

        Statistics stats = FileController.generateStatisticsAndOutputFiles(args);

        assertNotNull(stats);

        Path intsFile = tempDir.resolve("ints.txt");
        Path floatsFile = tempDir.resolve("floats.txt");
        Path stringsFile = tempDir.resolve("strings.txt");

        assertTrue(Files.exists(intsFile));
        assertTrue(Files.exists(floatsFile));
        assertTrue(Files.exists(stringsFile));

        assertEquals("123" + System.lineSeparator(), Files.readString(intsFile));
        assertEquals("1.528535047E-25" + System.lineSeparator(), Files.readString(floatsFile));
        assertEquals("Персик" + System.lineSeparator(), Files.readString(stringsFile));
    }

    @Test
    void generateStatisticsAndOutputFiles_withMultipleInputFiles() throws IOException {
        Path inputFile1 = tempDir.resolve("file1.txt");
        Path inputFile2 = tempDir.resolve("file2.txt");
        Files.write(inputFile1, List.of("123", "1.528535047E-25"));
        Files.write(inputFile2, List.of("Персик"));

        String[] systemArgs = String.format(
                "%s %s -o %s",
                inputFile1.toAbsolutePath(),
                inputFile2.toAbsolutePath(),
                tempDir.toAbsolutePath()
        ).split(" ");

        Arguments args = InputController.parse(systemArgs);

        Statistics stats = FileController.generateStatisticsAndOutputFiles(args);

        assertNotNull(stats);

        Path intsFile = tempDir.resolve("ints.txt");
        Path floatsFile = tempDir.resolve("floats.txt");
        Path stringsFile = tempDir.resolve("strings.txt");

        assertTrue(Files.exists(intsFile));
        assertTrue(Files.exists(floatsFile));
        assertTrue(Files.exists(stringsFile));

        assertEquals("123" + System.lineSeparator(), Files.readString(intsFile));
        assertEquals("1.528535047E-25" + System.lineSeparator(), Files.readString(floatsFile));
        assertEquals("Персик" + System.lineSeparator(), Files.readString(stringsFile));
    }

    @Test
    void generateStatisticsAndOutputFiles_shouldCreateFilesIfNeeded() throws IOException {
        Path inputFile = tempDir.resolve("file1.txt");
        Files.write(inputFile, List.of("123"));

        String[] systemArgs = String.format(
                "%s -o %s",
                inputFile.toAbsolutePath(),
                tempDir.toAbsolutePath()
        ).split(" ");

        Arguments args = InputController.parse(systemArgs);

        Statistics stats = FileController.generateStatisticsAndOutputFiles(args);

        assertNotNull(stats);

        Path intsFile = tempDir.resolve("ints.txt");
        Path floatsFile = tempDir.resolve("floats.txt");
        Path stringsFile = tempDir.resolve("strings.txt");

        assertTrue(Files.exists(intsFile));
        assertFalse(Files.exists(floatsFile));
        assertFalse(Files.exists(stringsFile));

        assertEquals("123" + System.lineSeparator(), Files.readString(intsFile));
    }

    @Test
    void generateStatisticsAndOutputFiles_shouldThrowZeroInputFilesException() {
        String[] systemArgs = new String[]{};

        Arguments args = InputController.parse(systemArgs);

        assertThrows(ZeroInputFilesException.class, () -> FileController.generateStatisticsAndOutputFiles(args));
    }


    @Test
    void generateStatisticsAndOutputFiles_shouldSkipIncorrectOutputPath() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Path inputFile = tempDir.resolve("file1.txt");
        Files.write(inputFile, List.of("123", "1.528535047E-25", "Персик"));

        String[] systemArgs = String.format(
                "%s -o %s",
                inputFile.toAbsolutePath(),
                "C:/Windows"
        ).split(" ");

        Arguments args = InputController.parse(systemArgs);

        assertTrue(outContent.toString().contains(OutputMessage.INCORRECT_OUTPUT_PATH.getValue("C:/Windows/")));
    }
}
