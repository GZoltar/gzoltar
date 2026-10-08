package org.gzoltar.java11;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class PredicateFeaturesTest {

  private final PredicateFeatures predicateFeatures = new PredicateFeatures();

  @Test
  public void testNonBlankValues() {
    List<String> values = Arrays.asList("gzoltar", "", "  ", "java");
    assertEquals(Arrays.asList("gzoltar", "java"), predicateFeatures.nonBlankValues(values));
  }

  @Test
  public void testNonNegativeValues() {
    List<Integer> numbers = Arrays.asList(-2, 3, -1, 5);
    assertEquals(Arrays.asList(3, 5), predicateFeatures.nonNegativeValues(numbers));
  }

}
