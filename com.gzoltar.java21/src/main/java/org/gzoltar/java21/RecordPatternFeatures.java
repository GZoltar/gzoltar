package org.gzoltar.java21;

/**
 * Record Patterns (JEP 440, finalized/standard in Java 21).
 *
 * A record's components can now be destructured directly in an
 * instanceof/switch pattern, instead of matching the record's type and then
 * manually calling its accessor methods.
 */
public class RecordPatternFeatures {

  public record Point(int x, int y) {}

  /** Destructuring via "instanceof" record pattern (no bug here). */
  public boolean isOrigin(Object obj) {
    if (obj instanceof Point(int x, int y)) {
      return x == 0 && y == 0;
    }
    return false;
  }

  /** Destructuring via "switch" record pattern, combined with guards. */
  public String quadrantOf(Object obj) {
    return switch (obj) {
      case Point(int x, int y) when x > 0 && y > 0 -> "quadrant I";
      case Point(int x, int y) when x < 0 && y > 0 -> "quadrant II";
      case Point(int x, int y) when x < 0 && y < 0 -> "quadrant III";
      case Point(int x, int y) when x > 0 && y < 0 -> "quadrant IV";
      case Point p -> "on an axis";
      default -> "not a point";
    };
  }

  /** Nested record pattern: a record containing another record. */
  public record Line(Point start, Point end) {}

  public int manhattanLength(Line line) {
    if (line instanceof Line(Point(int x1, int y1), Point(int x2, int y2))) {
      return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
    return -1;
  }
}
