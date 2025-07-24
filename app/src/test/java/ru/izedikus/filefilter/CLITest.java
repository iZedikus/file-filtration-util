package ru.izedikus.filefilter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CLITest {
  @Test
  void cliHasAGreetingFromLegacyOfPreviousTest() {
    CLI cli = new CLI();
    assertNotNull(cli.getGreeting(), "cli should have a greeting");
  }
}
