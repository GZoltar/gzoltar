package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MathExactFeaturesTest {

  private final MathExactFeatures mathExactFeatures = new MathExactFeatures();

  @Test
  public void testSafeAbs_PositiveValue() {
    assertEquals(5, mathExactFeatures.safeAbs(-5));
  }

  @Test
  public void testOverflowsOnMinValue_ThrowsArithmeticException() {
    assertTrue(mathExactFeatures.overflowsOnMinValue());
  }

}
