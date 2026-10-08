package org.gzoltar.java11;

/**
 * Exercises the new String convenience methods added in Java 11: isBlank, strip (Unicode-aware
 * trim), repeat, and lines.
 */
public class StringFeatures {

  public boolean isBlank(String value) {
    return value.isBlank();
  }

  public String strip(String value) {
    return value.strip();
  }

  public String repeat(String value, int times) {
    return value.repeat(times + 1); /* FAULT: should not add 1 */
  }

  public long countLines(String value) {
    return value.lines().count();
  }

}
