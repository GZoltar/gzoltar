package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NestFeaturesTest {

  private final NestFeatures nestFeatures = new NestFeatures();

  @Test
  public void testInnerCanReadOuterPrivateField() {
    NestFeatures.Inner inner = nestFeatures.newInner();
    assertEquals(42, inner.readOuterSecret());
  }

  @Test
  public void testIsNestmateOfInner() {
    assertTrue(nestFeatures.isNestmateOfInner());
  }

  @Test
  public void testNestHost_IsOuterClass() {
    assertEquals(NestFeatures.class, nestFeatures.nestHost());
  }

  @Test
  public void testNestMemberCount_IncludesOuterAndInner() {
    // The nest contains at least the outer class itself and the Inner class.
    assertTrue(nestFeatures.nestMemberCount() >= 2);
  }

}
