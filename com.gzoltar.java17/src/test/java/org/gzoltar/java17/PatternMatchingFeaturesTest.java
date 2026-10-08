package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternMatchingFeaturesTest {

  private final PatternMatchingFeatures patternMatchingFeatures = new PatternMatchingFeatures();

  @Test
  public void testDescribe_PositiveInt() {
    assertEquals("positive int: 5", patternMatchingFeatures.describe(5));
  }

  @Test
  public void testDescribe_NonPositiveInt() {
    assertEquals("non-positive int: -3", patternMatchingFeatures.describe(-3));
  }

  @Test
  public void testDescribe_Zero_ExposesFault() {
    // Zero is not positive; this exposes the injected FAULT (>= 0 instead of > 0).
    assertEquals("non-positive int: 0", patternMatchingFeatures.describe(0));
  }

  @Test
  public void testDescribe_NonBlankString() {
    assertEquals("non-blank string: gzoltar", patternMatchingFeatures.describe("gzoltar"));
  }

  @Test
  public void testDescribe_BlankString() {
    assertEquals("blank string", patternMatchingFeatures.describe("   "));
  }

  @Test
  public void testDescribe_UnknownType() {
    assertEquals("unknown: 3.14", patternMatchingFeatures.describe(3.14));
  }

}
