package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PatternMatchingSwitchFeaturesTest {

  private final PatternMatchingSwitchFeatures patternMatchingSwitchFeatures =
      new PatternMatchingSwitchFeatures();

  @Test
  public void testClassify_PositiveInt() {
    assertEquals("positive int", patternMatchingSwitchFeatures.classify(5));
  }

  @Test
  public void testClassify_NegativeInt() {
    assertEquals("negative int", patternMatchingSwitchFeatures.classify(-5));
  }

  @Test
  public void testClassify_Zero_ExposesFault() {
    // 0 must be classified as "zero", not "positive int".
    assertEquals("zero", patternMatchingSwitchFeatures.classify(0));
  }

  @Test
  public void testClassify_EmptyString() {
    assertEquals("empty string", patternMatchingSwitchFeatures.classify(""));
  }

  @Test
  public void testClassify_NonEmptyString() {
    assertEquals("non-empty string: hi", patternMatchingSwitchFeatures.classify("hi"));
  }

  @Test
  public void testClassify_Null() {
    assertEquals("null value", patternMatchingSwitchFeatures.classify(null));
  }

  @Test
  public void testClassify_Unknown() {
    assertEquals("unknown: 3.14", patternMatchingSwitchFeatures.classify(3.14));
  }

  @Test
  public void testDescribeType_Integer() {
    assertEquals("an Integer", patternMatchingSwitchFeatures.describeType(7));
  }

  @Test
  public void testDescribeType_String() {
    assertEquals("a String", patternMatchingSwitchFeatures.describeType("x"));
  }

  @Test
  public void testDescribeType_Double() {
    assertEquals("a Double", patternMatchingSwitchFeatures.describeType(1.5));
  }

  @Test
  public void testDescribeType_Null() {
    assertEquals("nothing", patternMatchingSwitchFeatures.describeType(null));
  }
}
