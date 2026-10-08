package org.gzoltar.java25;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.Test;

public class ModuleImportFeaturesTest {

  private final ModuleImportFeatures moduleImportFeatures = new ModuleImportFeatures();

  @Test
  public void testSortedUnique() {
    List<String> result = moduleImportFeatures.sortedUnique(List.of("b", "a", "b", "c", "a"));
    assertEquals(List.of("a", "b", "c"), result);
  }

  @Test
  public void testLengthsOf_ExposesFault() {
    Map<String, Integer> result = moduleImportFeatures.lengthsOf(List.of("hi", "world"));
    assertEquals(Integer.valueOf(2), result.get("hi"));
    assertEquals(Integer.valueOf(5), result.get("world"));
  }

  @Test
  public void testToSortedList() {
    assertEquals(List.of(1, 2, 3, 4), moduleImportFeatures.toSortedList(new int[] {3, 1, 4, 2}));
  }

}
