package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordFeaturesTest {

  private final RecordFeatures recordFeatures = new RecordFeatures();

  @Test
  public void testAccessors() {
    RecordFeatures.Range range = recordFeatures.createRange(2, 5);
    assertEquals(2, range.start());
    assertEquals(5, range.end());
    assertEquals(3, range.length());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCompactConstructor_RejectsInvalidRange() {
    recordFeatures.createRange(5, 2);
  }

  @Test
  public void testEquals_SameComponents() {
    assertTrue(recordFeatures.areEqual(
        recordFeatures.createRange(1, 4), recordFeatures.createRange(1, 4)));
  }

  @Test
  public void testEquals_DifferentComponents() {
    assertFalse(recordFeatures.areEqual(
        recordFeatures.createRange(1, 4), recordFeatures.createRange(1, 5)));
  }

  @Test
  public void testDescribe_ContainsComponents() {
    String description = recordFeatures.describe(recordFeatures.createRange(1, 4));
    assertTrue(description.contains("1"));
    assertTrue(description.contains("4"));
  }

}
