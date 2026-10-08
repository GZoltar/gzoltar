package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class VarFeaturesTest {

  private final VarFeatures varFeatures = new VarFeatures();

  @Test
  public void testCombineWithVarLambda() {
    assertEquals(7, varFeatures.combineWithVarLambda(3, 4));
  }

}
