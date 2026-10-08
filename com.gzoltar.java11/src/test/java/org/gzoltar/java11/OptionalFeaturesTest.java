package org.gzoltar.java11;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Test;

public class OptionalFeaturesTest {

  private final OptionalFeatures optionalFeatures = new OptionalFeatures();

  @Test
  public void testIsEmpty_True() {
    assertTrue(optionalFeatures.isEmpty(Optional.empty()));
  }

  @Test
  public void testIsEmpty_False() {
    assertFalse(optionalFeatures.isEmpty(Optional.of("gzoltar")));
  }

}
