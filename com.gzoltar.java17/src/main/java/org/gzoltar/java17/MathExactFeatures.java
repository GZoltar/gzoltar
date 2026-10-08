package org.gzoltar.java17;

/**
 * Exercises Math#absExact (added in Java 15): unlike the classic Math.abs(int), which silently
 * returns Integer.MIN_VALUE unchanged (a well-known overflow gotcha, since +2147483648 does not
 * fit in an int), absExact throws an ArithmeticException instead of returning a wrong answer.
 */
public class MathExactFeatures {

  public int safeAbs(int value) {
    return Math.absExact(value);
  }

  public boolean overflowsOnMinValue() {
    try {
      Math.absExact(Integer.MIN_VALUE);
      return false;
    } catch (ArithmeticException e) {
      return true;
    }
  }

}
