package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SealedFeaturesTest {

  private final SealedFeatures sealedFeatures = new SealedFeatures();

  @Test
  public void testArea_Circle() {
    SealedFeatures.Circle circle = new SealedFeatures.Circle(2.0);
    assertEquals(Math.PI * 4, sealedFeatures.area(circle), 0.0001);
  }

  @Test
  public void testArea_Rectangle() {
    SealedFeatures.Rectangle rectangle = new SealedFeatures.Rectangle(3.0, 4.0);
    assertEquals(12.0, sealedFeatures.area(rectangle), 0.0001);
  }

}
