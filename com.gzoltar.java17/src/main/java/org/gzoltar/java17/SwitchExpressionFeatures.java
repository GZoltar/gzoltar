package org.gzoltar.java17;

/**
 * Exercises Switch Expressions (JEP 361, finalized in Java 14): arrow-labeled cases that produce
 * a value directly (no fall-through, no `break` needed), plus `yield` for multi-statement
 * branches.
 */
public class SwitchExpressionFeatures {

  public enum Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
  }

  public String dayType(Day day) {
    return switch (day) {
      case SATURDAY, SUNDAY -> "weekend";
      case MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY -> "weekday";
    };
  }

  public int lettersInDayName(Day day) {
    return switch (day) {
      case MONDAY -> 6;
      case TUESDAY -> 7;
      case WEDNESDAY -> 9;
      case THURSDAY -> 8;
      case FRIDAY -> 6;
      case SATURDAY -> 8;
      case SUNDAY -> {
        int length = "SUNDAY".length();
        yield length + 1; /* FAULT: should just be length, not length + 1 */
      }
    };
  }

}
