package ru.izedikus.filefilter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.io.TempDir;
import ru.izedikus.filefilter.input.InputController;
import ru.izedikus.filefilter.models.Arguments;

import static org.junit.jupiter.api.Assertions.*;
import static ru.izedikus.filefilter.output.OutputMessage.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class InputControllerTest {
    ByteArrayOutputStream outContent;

    @TempDir
    Path tempDir;

    private String getTempDir() {
        return tempDir.toString().replace('\\', '/') + "/";
    }

    private int fileCount = 0;

    private String getInputFilePath() {
        String tempDirPath = getTempDir();
        Path filePath = fileCount == 0
                ? Path.of(tempDirPath.concat("\\file.txt"))
                : Path.of(tempDirPath.concat("\\file").concat(String.valueOf(fileCount).concat(".txt")));
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileCount++;
        return filePath.toAbsolutePath().toString().replace('\\', '/');
    }

    @BeforeEach
    void setup() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restore() {
        System.setOut(System.out);
    }

    @Test
    void noArguments() {
        String[] args = new String[]{};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of(), parsedArgs.inputFiles());
        assertEquals("", parsedArgs.outputPathIfBeenFlagged());
        assertEquals("", parsedArgs.prefixIfBeenFlagged());
        assertFalse(parsedArgs.addFlag());
        assertFalse(parsedArgs.fullStatsFlag());
    }

    @Test
    void inputFiles_correctParse() {
        String filePath = getInputFilePath();

        String[] args = new String[]{filePath};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of(filePath), parsedArgs.inputFiles());
    }

    @Test
    void inputFiles_withoutExtension() {
        String filePath = getInputFilePath();

        String fileWithoutExtension = filePath.substring(0, filePath.length() - 4);

        String[] args = new String[]{fileWithoutExtension};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of(filePath), parsedArgs.inputFiles());
    }

    @Test
    void incorrectFlag_skip() {
        String[] args = new String[]{"-x"};
        InputController.parse(args);

        assertTrue(outContent.toString().contains(INCORRECT_FLAG.getValue("-x")));
    }

    @Test
    void addFlag_setCorrectly() {
        String[] args1 = new String[]{"-a"};
        String[] args2 = new String[]{"--add"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.addFlag());
        assertTrue(parsedArgs2.addFlag());
    }

    @Test
    void addFlag_manyFlags() {
        String[] args1 = new String[]{"-a", "-a"};
        String[] args2 = new String[]{"--add", "--add"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.addFlag());
        assertTrue(parsedArgs2.addFlag());

        assertTrue(outContent.toString().contains(MANY_ADDS.getValue()));
    }

    @Test
    void fullFlag_setCorrectly() {
        String[] args1 = new String[]{"-f"};
        String[] args2 = new String[]{"--full"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.fullStatsFlag());
        assertTrue(parsedArgs2.fullStatsFlag());
    }

    @Test
    void fullFlag_manyFlags() {
        String[] args1 = new String[]{"-f", "-f"};
        String[] args2 = new String[]{"--full", "--full"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.fullStatsFlag());
        assertTrue(parsedArgs2.fullStatsFlag());

        assertTrue(outContent.toString().contains(MANY_FULLS.getValue()));
    }

    @Test
    void shortFlag_setCorrectly() {
        String[] args1 = new String[]{"-s"};
        String[] args2 = new String[]{"--short"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertFalse(parsedArgs1.fullStatsFlag());
        assertFalse(parsedArgs2.fullStatsFlag());
    }

    @Test
    void shortFlag_manyFlags() {
        String[] args1 = new String[]{"-s", "-s"};
        String[] args2 = new String[]{"--short", "--short"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertFalse(parsedArgs1.fullStatsFlag());
        assertFalse(parsedArgs2.fullStatsFlag());

        assertTrue(outContent.toString().contains(MANY_SHORTS.getValue()));
    }

    @Test
    void fullAndShortFlags_conflict() {
        String[] args = new String[]{"-f", "-s"};
        Arguments parsedArgs = InputController.parse(args);

        assertTrue(parsedArgs.fullStatsFlag());

        assertTrue(outContent.toString().contains(SHORT_AND_FULL.getValue()));
    }

    @Test
    void shortAndFullFlags_conflict() {
        String[] args = new String[]{"-s", "-f"};
        Arguments parsedArgs = InputController.parse(args);

        assertFalse(parsedArgs.fullStatsFlag());

        assertTrue(outContent.toString().contains(SHORT_AND_FULL.getValue()));
    }

    @Test
    void outputPath_formatCorrectly() {
        String outputPath = getTempDir();

        String[] args1 = new String[]{"-o", outputPath};
        String[] args2 = new String[]{"--output", outputPath};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(outputPath, parsedArgs1.outputPathIfBeenFlagged());
        assertEquals(outputPath, parsedArgs2.outputPathIfBeenFlagged());
    }

    @Test
    void outputPath_formatIncorrectly() {
        String outputPath = getTempDir();
        String incorrectOutputPath = new StringBuilder(outputPath).insert(outputPath.length() / 2, ":").toString();

        String[] args1 = new String[]{"-o", incorrectOutputPath};
        String[] args2 = new String[]{"--output", incorrectOutputPath};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("", parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(INCORRECT_OUTPUT_PATH.getValue(incorrectOutputPath)));
    }

    @Test
    void outputPath_withoutEndingSlash() {
        String outputPath = getTempDir();

        String[] args1 = new String[]{"-o", outputPath.substring(0, outputPath.length() - 1)};
        String[] args2 = new String[]{"--output", outputPath};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(outputPath, parsedArgs1.outputPathIfBeenFlagged());
        assertEquals(outputPath, parsedArgs2.outputPathIfBeenFlagged());
    }

    @Test
    void outputPath_missingArgument_skip() {
        String filePath = getInputFilePath();

        String[] args1 = new String[]{filePath, "-o"};
        String[] args2 = new String[]{filePath, "--output"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(List.of(filePath), parsedArgs1.inputFiles());
        assertEquals(List.of(filePath), parsedArgs2.inputFiles());
        assertEquals("", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("", parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(MISSING_OUTPUT_PATH.getValue()));
    }

    @Test
    void outputFlag_manyPaths() {
        String outputPath1 = getTempDir();
        String outputPath2 = outputPath1 + "into/";

        String[] args1 = new String[]{"-o", outputPath1, "-o", outputPath2};
        String[] args2 = new String[]{"--output", outputPath1, "--output", outputPath2};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(outputPath1, parsedArgs1.outputPathIfBeenFlagged());
        assertEquals(outputPath1, parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(MANY_OUTPUTS.getValue()));
    }

    @Test
    void prefix_formatCorrectly() {
        String[] args1 = new String[]{"-p", "prefix_"};
        String[] args2 = new String[]{"--prefix", "prefix_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("prefix_", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("prefix_", parsedArgs2.prefixIfBeenFlagged());
    }

    @Test
    void prefix_formatIncorrectly() {
        String[] args1 = new String[]{"-p", "pre*fix_"};
        String[] args2 = new String[]{"--prefix", "pre*fix_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(INCORRECT_PREFIX.getValue()));
    }

    @Test
    void prefixFlag_missingArgument() {
        String filePath = getInputFilePath();

        String[] args1 = new String[]{filePath, "-p"};
        String[] args2 = new String[]{filePath, "--prefix"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(List.of(filePath), parsedArgs1.inputFiles());
        assertEquals(List.of(filePath), parsedArgs2.inputFiles());
        assertEquals("", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(MISSING_PREFIX.getValue()));
    }

    @Test
    void prefixFlag_manyPrefixes() {
        String[] args1 = new String[]{"-p", "prefix1_", "-p", "prefix2_"};
        String[] args2 = new String[]{"--prefix", "prefix1_", "--prefix", "prefix2_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("prefix1_", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("prefix1_", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(MANY_PREFIXES.getValue()));
    }

    @Test
    void mixedFlagsWithCorrectAndIncorrectFiles() {
        String outputPath = getTempDir();

        String filePath1 = getInputFilePath();

        String filePath2 = getInputFilePath();
        filePath2 = filePath2.substring(0, filePath2.length() / 2) + ":" + filePath2.substring(filePath2.length() / 2);

        String[] args = new String[]{filePath1, "-a", "-o", outputPath, filePath2, "-p", "prefix_"};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of(filePath1), parsedArgs.inputFiles());
        assertTrue(parsedArgs.addFlag());
        assertEquals(outputPath, parsedArgs.outputPathIfBeenFlagged());
        assertEquals("prefix_", parsedArgs.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(INPUT_FILE_NOT_FOUND.getValue(filePath2)));
    }
}
