package org.gzoltar.java17;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class StreamToListFeaturesTest {

  private final StreamToListFeatures streamToListFeatures = new StreamToListFeatures();

  @Test
  public void testSquares() {
    List<Integer> numbers = Arrays.asList(1, 2, 3);
    assertEquals(Arrays.asList(1, 4, 9), streamToListFeatures.squares(numbers));
  }

  @Test
  public void testSquares_ResultIsImmutable() {
    List<Integer> result = streamToListFeatures.squares(Arrays.asList(1, 2));
    assertTrue(streamToListFeatures.isImmutable(result));
  }

}
