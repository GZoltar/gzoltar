package org.gzoltar.java21;

/**
 * Math.clamp() / StrictMath.clamp() (JDK-8301226, added in Java 21).
 *
 * Clamps a value into the inclusive range [min, max] in a single call,
 * instead of the previous idiom of nesting Math.min(Math.max(value, min), max).
 * Overloads exist for int, long, float and double.
 */
public class MathClampFeatures {

  public int clampInt(int value, int min, int max) {
    return Math.clamp(value, min, max);
  }

  public long clampLong(long value, long min, long max) {
    return Math.clamp(value, min, max);
  }

  public double clampDouble(double value, double min, double max) {
    return Math.clamp(value, min, max);
  }

  /** Convenience wrapper: clamps a percentage-like value into [0, 100]. */
  public int clampToPercentage(int value) {
    return Math.clamp(value, 0, 100);
  }
}
