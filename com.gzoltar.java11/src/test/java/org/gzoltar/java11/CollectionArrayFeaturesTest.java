package org.gzoltar.java11;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class CollectionArrayFeaturesTest {

  private final CollectionArrayFeatures collectionArrayFeatures = new CollectionArrayFeatures();

  @Test
  public void testToStringArray() {
    List<String> values = Arrays.asList("g", "zoltar");
    assertArrayEquals(new String[] {"g", "zoltar"}, collectionArrayFeatures.toStringArray(values));
  }

  @Test
  public void testTotalLength() {
    List<String> values = Arrays.asList("gzoltar", "java");
    assertEquals(11, collectionArrayFeatures.totalLength(values));
  }

}
