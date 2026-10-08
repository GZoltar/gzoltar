package org.gzoltar.java25;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FlexibleConstructorBodiesTest {

  private final FlexibleConstructorBodies flexibleConstructorBodies = new FlexibleConstructorBodies();

  @Test
  public void testCreateValid() {
    assertEquals(5, flexibleConstructorBodies.createValid(5));
  }

  @Test
  public void testRejectsNegative() {
    assertTrue(flexibleConstructorBodies.rejectsNonPositive(-1));
  }

  @Test
  public void testRejectsZero_ExposesFault() {
    // 0 is not strictly positive, so it must be rejected too.
    assertTrue(flexibleConstructorBodies.rejectsNonPositive(0));
  }

}
