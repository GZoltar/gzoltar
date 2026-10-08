package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class VirtualThreadFeaturesTest {

  private final VirtualThreadFeatures virtualThreadFeatures = new VirtualThreadFeatures();

  @Test
  public void testCurrentThreadIsVirtualWhenUsingVirtualExecutor() throws Exception {
    assertTrue(virtualThreadFeatures.currentThreadIsVirtualWhenUsingVirtualExecutor());
  }

  @Test
  public void testSumOfSquares() throws Exception {
    assertEquals(1 + 4 + 9 + 16, virtualThreadFeatures.sumOfSquares(List.of(1, 2, 3, 4)));
  }

  @Test
  public void testDirectlyStartedThreadIsVirtual() throws Exception {
    assertTrue(virtualThreadFeatures.directlyStartedThreadIsVirtual());
  }
}
