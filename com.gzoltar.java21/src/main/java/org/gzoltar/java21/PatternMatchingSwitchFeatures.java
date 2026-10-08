package org.gzoltar.java21;

/**
 * Pattern Matching for switch (JEP 441, finalized/standard in Java 21).
 *
 * Before Java 21 this required chained if-instanceof checks. Now a switch
 * expression can match on the runtime type of the selector, bind a pattern
 * variable per case, and further refine cases with a "when" guard clause.
 */
public class PatternMatchingSwitchFeatures {

  /**
   * Classifies an arbitrary object using type patterns + guarded patterns.
   *
   * NOTE: contains an intentional FAULT. The guard for the "positive int"
   * case uses {@code i >= 0} instead of {@code i > 0}, so 0 is misclassified
   * as "positive int" instead of "zero".
   */
  public String classify(Object obj) {
    return switch (obj) {
      case null -> "null value";
      case Integer i when i >= 0 -> "positive int"; // FAULT: should be i > 0
      case Integer i when i < 0 -> "negative int";
      case Integer i -> "zero";
      case String s when s.isEmpty() -> "empty string";
      case String s -> "non-empty string: " + s;
      default -> "unknown: " + obj;
    };
  }

  /** A case with no guard at all - simple type pattern matching. */
  public String describeType(Object obj) {
    return switch (obj) {
      case Integer i -> "an Integer";
      case String s -> "a String";
      case Double d -> "a Double";
      case null -> "nothing";
      default -> "something else";
    };
  }
}
