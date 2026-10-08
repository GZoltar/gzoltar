package org.gzoltar.java25;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ScopedValueFeaturesTest {

  private final ScopedValueFeatures scopedValueFeatures = new ScopedValueFeatures();

  @Test
  public void testReadInsideScope() throws Exception {
    assertEquals("alice", scopedValueFeatures.readInsideScope("alice"));
  }

  @Test
  public void testIsBoundOutsideScope() {
    assertFalse(scopedValueFeatures.isBoundOutsideScope());
  }

  @Test
  public void testDefaultWhenUnbound() {
    assertEquals("anonymous", scopedValueFeatures.defaultWhenUnbound("anonymous"));
  }

  @Test
  public void testNestedScopedCall() throws Exception {
    assertEquals("outer:inner", scopedValueFeatures.nestedScopedCall("outer", "inner"));
  }

}
