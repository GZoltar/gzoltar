package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CompletableFutureFeaturesTest {

  private final CompletableFutureFeatures completableFutureFeatures =
      new CompletableFutureFeatures();

  @Test
  public void testSquare() throws Exception {
    assertEquals(49, completableFutureFeatures.square(7));
  }

  @Test
  public void testCombineGreeting() throws Exception {
    assertEquals(
        "Hello Ruken & Hello GZoltar",
        completableFutureFeatures.combineGreeting("Ruken", "GZoltar"));
  }

  @Test
  public void testChain() throws Exception {
    // (5 + 1) * 2 = 12
    assertEquals(12, completableFutureFeatures.chain(5));
  }

  @Test
  public void testRecoverFromFailure_WhenFails() throws Exception {
    assertEquals("recovered: boom", completableFutureFeatures.recoverFromFailure(true));
  }

  @Test
  public void testRecoverFromFailure_WhenSucceeds() throws Exception {
    assertEquals("ok", completableFutureFeatures.recoverFromFailure(false));
  }

}
