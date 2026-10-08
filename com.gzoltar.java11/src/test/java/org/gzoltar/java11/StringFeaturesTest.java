package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringFeaturesTest {

  private final StringFeatures stringFeatures = new StringFeatures();

  @Test
  public void testIsBlank_True() {
    assertTrue(stringFeatures.isBlank("   "));
  }

  @Test
  public void testIsBlank_False() {
    assertFalse(stringFeatures.isBlank("gzoltar"));
  }

  @Test
  public void testStrip_RemovesUnicodeWhitespace() {
    assertEquals("gzoltar", stringFeatures.strip("  gzoltar  "));
  }

  @Test
  public void testRepeat_ExactCount() {
    // "ab" repeated 3 times should be "ababab" (6 chars); this exposes the injected FAULT.
    assertEquals("ababab", stringFeatures.repeat("ab", 3));
  }

  @Test
  public void testCountLines() {
    assertEquals(3, stringFeatures.countLines("line1\nline2\nline3"));
  }

}
