package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordPatternFeaturesTest {

  private final RecordPatternFeatures recordPatternFeatures = new RecordPatternFeatures();

  @Test
  public void testIsOrigin_True() {
    assertTrue(recordPatternFeatures.isOrigin(new RecordPatternFeatures.Point(0, 0)));
  }

  @Test
  public void testIsOrigin_False() {
    assertFalse(recordPatternFeatures.isOrigin(new RecordPatternFeatures.Point(1, 0)));
  }

  @Test
  public void testIsOrigin_NotAPoint() {
    assertFalse(recordPatternFeatures.isOrigin("not a point"));
  }

  @Test
  public void testQuadrantOf_QuadrantI() {
    assertEquals("quadrant I", recordPatternFeatures.quadrantOf(new RecordPatternFeatures.Point(3, 4)));
  }

  @Test
  public void testQuadrantOf_QuadrantII() {
    assertEquals("quadrant II", recordPatternFeatures.quadrantOf(new RecordPatternFeatures.Point(-3, 4)));
  }

  @Test
  public void testQuadrantOf_QuadrantIII() {
    assertEquals("quadrant III", recordPatternFeatures.quadrantOf(new RecordPatternFeatures.Point(-3, -4)));
  }

  @Test
  public void testQuadrantOf_QuadrantIV() {
    assertEquals("quadrant IV", recordPatternFeatures.quadrantOf(new RecordPatternFeatures.Point(3, -4)));
  }

  @Test
  public void testQuadrantOf_OnAxis() {
    assertEquals("on an axis", recordPatternFeatures.quadrantOf(new RecordPatternFeatures.Point(0, 5)));
  }

  @Test
  public void testQuadrantOf_NotAPoint() {
    assertEquals("not a point", recordPatternFeatures.quadrantOf(42));
  }

  @Test
  public void testManhattanLength() {
    RecordPatternFeatures.Line line = new RecordPatternFeatures.Line(
        new RecordPatternFeatures.Point(0, 0), new RecordPatternFeatures.Point(3, 4));
    assertEquals(7, recordPatternFeatures.manhattanLength(line));
  }
}
