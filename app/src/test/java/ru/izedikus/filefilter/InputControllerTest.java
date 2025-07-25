package ru.izedikus.filefilter;

import org.junit.jupiter.api.Test;

import ru.izedikus.filefilter.models.Arguments;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class InputControllerTest {

  @Test
  void testParseInputFiles() {
    String[] args = { "file1.txt", "file2.txt" };
    Arguments parsedArgs = InputController.parse(args);

    assertEquals(List.of(args), parsedArgs.inputFiles());
  }

  @Test
  void testParseAddFlag() {
    String[] args1 = { "-a" };
    String[] args2 = { "--add" };

    Arguments parsedArgs1 = InputController.parse(args1);
    Arguments parsedArgs2 = InputController.parse(args2);

    assertEquals(true, parsedArgs1.addFlag());
    assertEquals(true, parsedArgs2.addFlag());
  }

  @Test
  void testParseFullFlag() {
    String[] args1 = { "-f" };
    String[] args2 = { "--full" };

    Arguments parsedArgs1 = InputController.parse(args1);
    Arguments parsedArgs2 = InputController.parse(args2);

    assertEquals(true, parsedArgs1.fullStatsFlag());
    assertEquals(true, parsedArgs2.fullStatsFlag());
  }

  @Test
  void testParseOutputFlag() {
    String[] args1 = { "-o", "/internalFolder" };
    String[] args2 = { "--output", "/internalFolder" };

    Arguments parsedArgs1 = InputController.parse(args1);
    Arguments parsedArgs2 = InputController.parse(args2);

    assertEquals("/internalFolder", parsedArgs1.outputPathIfBeenFlagged());
    assertEquals("/internalFolder", parsedArgs2.outputPathIfBeenFlagged());
  }

  @Test
  void testParsePrefixFlag() {
    String[] args1 = { "-p", "prefix_" };
    String[] args2 = { "--prefix", "prefix_" };

    Arguments parsedArgs1 = InputController.parse(args1);
    Arguments parsedArgs2 = InputController.parse(args2);

    assertEquals("prefix_", parsedArgs1.prefixIfBeenFlagged());
    assertEquals("prefix_", parsedArgs2.prefixIfBeenFlagged());
  }

  @Test
  void testParseAllFlagsAndFiles() {
    String[] args = { "file1.txt", "-a", "-f", "--output", "/internalFolder", "-p", "prefix_", "file2.txt" };

    Arguments parsedArgs = InputController.parse(args);
    Arguments expectedArgs = new Arguments(List.of("file1.txt", "file2.txt"), "/internalFolder", "prefix_", true,
        true);

    assertEquals(expectedArgs, parsedArgs);
  }
}
