package ru.izedikus.filefilter;

import org.junit.jupiter.api.Test;

import ru.izedikus.filefilter.input.InputController;
import ru.izedikus.filefilter.models.Arguments;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import ru.izedikus.filefilter.output.OutputMessage;

class InputControllerTest {

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
    void inputFiles_withoutExtension() {
        String[] args = new String[]{"file1"};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of("file1.txt"), parsedArgs.inputFiles());
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
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-a", "-a"};
        String[] args2 = new String[]{"--add", "--add"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.addFlag());
        assertTrue(parsedArgs2.addFlag());

        assertTrue(outContent.toString().contains(OutputMessage.MANY_ADDS.getValue()));
        System.setOut(System.out);
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
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-f", "-f"};
        String[] args2 = new String[]{"--full", "--full"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertTrue(parsedArgs1.fullStatsFlag());
        assertTrue(parsedArgs2.fullStatsFlag());

        assertTrue(outContent.toString().contains(OutputMessage.MANY_FULLS.getValue()));
        System.setOut(System.out);
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
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-s", "-s"};
        String[] args2 = new String[]{"--short", "--short"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertFalse(parsedArgs1.fullStatsFlag());
        assertFalse(parsedArgs2.fullStatsFlag());

        assertTrue(outContent.toString().contains(OutputMessage.MANY_SHORTS.getValue()));
        System.setOut(System.out);
    }

    @Test
    void fullAndShortFlags_conflict() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[]{"-f", "-s"};
        Arguments parsedArgs = InputController.parse(args);

        assertTrue(parsedArgs.fullStatsFlag());

        assertTrue(outContent.toString().contains(OutputMessage.SHORT_AND_FULL.getValue()));
        System.setOut(System.out);
    }

    @Test
    void shortAndFullFlags_conflict() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[]{"-s", "-f"};
        Arguments parsedArgs = InputController.parse(args);

        assertFalse(parsedArgs.fullStatsFlag());

        assertTrue(outContent.toString().contains(OutputMessage.SHORT_AND_FULL.getValue()));
        System.setOut(System.out);
    }

    @Test
    void outputPath_formatCorrectly() {
        String[] args1 = new String[]{"-o", "path/"};
        String[] args2 = new String[]{"--output", "path/"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("path/", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("path/", parsedArgs2.outputPathIfBeenFlagged());
    }

    @Test
    void outputPath_formatIncorrectly() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-o", "pa*th/"};
        String[] args2 = new String[]{"--output", "pa*th/"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("", parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.INCORRECT_OUTPUT_PATH.getValue()));
        System.setOut(System.out);
    }

    @Test
    void outputPath_withoutEndingSlash() {
        String[] args1 = new String[]{"-o", "path"};
        String[] args2 = new String[]{"--output", "path"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("path/", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("path/", parsedArgs2.outputPathIfBeenFlagged());
    }

    @Test
    void outputPath_missingArgument_skip() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"file1", "-o", "-h"};
        String[] args2 = new String[]{"file1", "--output"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(List.of("file1.txt"), parsedArgs1.inputFiles());
        assertEquals(List.of("file1.txt"), parsedArgs2.inputFiles());
        assertEquals("", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("", parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.MISSING_OUTPUT_PATH.getValue()));
        System.setOut(System.out);
    }

    @Test
    void outputFlag_manyPaths() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-o", "path1", "-o", "-path2"};
        String[] args2 = new String[]{"--output", "path1", "--output", "path2"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("path1/", parsedArgs1.outputPathIfBeenFlagged());
        assertEquals("path1/", parsedArgs2.outputPathIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.MANY_OUTPUTS.getValue()));
        System.setOut(System.out);
    }

    @Test
    void prefix_formatCorrectly() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-p", "prefix_"};
        String[] args2 = new String[]{"--prefix", "prefix_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("prefix_", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("prefix_", parsedArgs2.prefixIfBeenFlagged());
    }

    @Test
    void prefix_formatIncorrectly() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-p", "pre*fix_"};
        String[] args2 = new String[]{"--prefix", "pre*fix_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.INCORRECT_PREFIX.getValue()));
        System.setOut(System.out);
    }

    @Test
    void prefixFlag_missingArgument() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"file1", "-p"};
        String[] args2 = new String[]{"file1", "--prefix"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals(List.of("file1.txt"), parsedArgs1.inputFiles());
        assertEquals(List.of("file1.txt"), parsedArgs2.inputFiles());
        assertEquals("", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.MISSING_PREFIX.getValue()));
        System.setOut(System.out);
    }

    @Test
    void prefixFlag_manyPrefixes() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args1 = new String[]{"-p", "prefix1_", "-p", "prefix2_"};
        String[] args2 = new String[]{"--prefix", "prefix1_", "--prefix", "prefix2_"};
        Arguments parsedArgs1 = InputController.parse(args1);
        Arguments parsedArgs2 = InputController.parse(args2);

        assertEquals("prefix1_", parsedArgs1.prefixIfBeenFlagged());
        assertEquals("prefix1_", parsedArgs2.prefixIfBeenFlagged());

        assertTrue(outContent.toString().contains(OutputMessage.MANY_PREFIXES.getValue()));
        System.setOut(System.out);
    }

    @Test
    void mixedFlagsAndFiles() {
        String[] args = new String[]{"file1", "-a", "-o", "path", "file2", "-p", "prefix_", "-s"};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of("file1.txt", "file2.txt"), parsedArgs.inputFiles());
        assertTrue(parsedArgs.addFlag());
        assertEquals("path/", parsedArgs.outputPathIfBeenFlagged());
        assertEquals("prefix_", parsedArgs.prefixIfBeenFlagged());
    }

    @Test
    void incorrectFlag_skip() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[]{"-x", "file1"};
        Arguments parsedArgs = InputController.parse(args);

        assertEquals(List.of("file1.txt"), parsedArgs.inputFiles());

        assertTrue(outContent.toString().contains(OutputMessage.INCORRECT_FLAG.getValue("-x")));
        System.setOut(System.out);
    }

    @Test
    void inputFiles_incorrectFileNameException() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String[] args = new String[]{"fi*le1"};
        Arguments parsedArgs = InputController.parse(args);

        assertTrue(parsedArgs.inputFiles().isEmpty());

        assertTrue(outContent.toString().contains(OutputMessage.INCORRECT_TXT_FILE.getValue()));
        System.setOut(System.out);
    }
}
