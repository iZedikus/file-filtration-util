package ru.izedikus.filefilter;

public class CLI {
  public String getGreeting() {
    return "Hello CLI!";
  }

  public static void main(String[] args) {
    System.out.println(new CLI().getGreeting());
  }
}
