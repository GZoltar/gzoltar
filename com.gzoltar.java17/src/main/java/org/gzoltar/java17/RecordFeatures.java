package org.gzoltar.java17;

/**
 * Exercises Records (JEP 395, finalized in Java 16): a record auto-generates a canonical
 * constructor, accessors, equals/hashCode, and toString from its component list. This class also
 * shows a compact constructor used for validation.
 */
public class RecordFeatures {

  public record Range(int start, int end) {

    public Range {
      if (start > end) {
        throw new IllegalArgumentException("start must not be greater than end");
      }
    }

    public int length() {
      return end - start;
    }

  }

  public Range createRange(int start, int end) {
    return new Range(start, end);
  }

  public boolean areEqual(Range a, Range b) {
    return a.equals(b); // records get equals()/hashCode() for free, based on their components
  }

  public String describe(Range range) {
    return range.toString(); // records get a readable toString() for free
  }

}
