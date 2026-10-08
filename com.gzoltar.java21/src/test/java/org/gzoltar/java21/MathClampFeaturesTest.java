package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathClampFeaturesTest {

  private final MathClampFeatures mathClampFeatures = new MathClampFeatures();

  @Test
  public void testClampInt_WithinRange() {
    assertEquals(5, mathClampFeatures.clampInt(5, 0, 10));
  }

  @Test
  public void testClampInt_BelowMin() {
    assertEquals(0, mathClampFeatures.clampInt(-5, 0, 10));
  }

  @Test
  public void testClampInt_AboveMax() {
    assertEquals(10, mathClampFeatures.clampInt(15, 0, 10));
  }

  @Test
  public void testClampLong() {
    assertEquals(100L, mathClampFeatures.clampLong(500L, 0L, 100L));
  }

  @Test
  public void testClampDouble() {
    assertEquals(2.5, mathClampFeatures.clampDouble(2.5, 0.0, 10.0), 0.0001);
  }

  @Test
  public void testClampToPercentage_Over100() {
    assertEquals(100, mathClampFeatures.clampToPercentage(150));
  }

  @Test
  public void testClampToPercentage_Negative() {
    assertEquals(0, mathClampFeatures.clampToPercentage(-20));
  }
}
