package org.gzoltar.java17;

/**
 * Exercises Pattern Matching for instanceof (JEP 394, finalized in Java 16): the cast variable is
 * declared right inside the instanceof check, and the compiler flow-scopes it so it is only
 * usable where it's definitely been matched -- no separate cast statement needed.
 */
public class PatternMatchingFeatures {

  public String describe(Object value) {
    if (value instanceof Integer i && i >= 0) { /* FAULT: should be strictly greater than 0 */
      return "positive int: " + i;
    } else if (value instanceof Integer i) {
      return "non-positive int: " + i;
    } else if (value instanceof String s && !s.isBlank()) {
      return "non-blank string: " + s;
    } else if (value instanceof String) {
      return "blank string";
    }
    return "unknown: " + value;
  }

}
