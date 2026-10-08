package org.gzoltar.java17;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RandomGeneratorFeaturesTest {

  private final RandomGeneratorFeatures randomGeneratorFeatures = new RandomGeneratorFeatures();

  @Test
  public void testSameSeedProducesSameValue() {
    assertTrue(randomGeneratorFeatures.sameSeedProducesSameValue(42L));
  }

  @Test
  public void testValueWithinBound() {
    assertTrue(randomGeneratorFeatures.valueWithinBound(42L, 100));
  }

}
