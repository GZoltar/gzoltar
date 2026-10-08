package org.gzoltar.java21;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import org.junit.Test;

public class SequencedCollectionFeaturesTest {

  private final SequencedCollectionFeatures sequencedCollectionFeatures =
      new SequencedCollectionFeatures();

  @Test
  public void testFirst() {
    assertEquals(1, sequencedCollectionFeatures.first(List.of(1, 2, 3)));
  }

  @Test
  public void testLast() {
    assertEquals(3, sequencedCollectionFeatures.last(List.of(1, 2, 3)));
  }

  @Test
  public void testReversedView() {
    assertEquals(List.of(3, 2, 1), sequencedCollectionFeatures.reversedView(List.of(1, 2, 3)));
  }

  @Test
  public void testSecondToLast_ExposesFault() {
    // The second-to-last element of [1, 2, 3, 4] must be 3, not 4.
    assertEquals(3, sequencedCollectionFeatures.secondToLast(List.of(1, 2, 3, 4)));
  }

  @Test
  public void testInsertAtBothEnds() {
    SequencedMap<String, Integer> map =
        sequencedCollectionFeatures.insertAtBothEnds("first", 1, "last", 9);
    assertEquals("first", map.firstEntry().getKey());
    assertEquals("last", map.lastEntry().getKey());
  }

  @Test
  public void testFirstEntry() {
    SequencedMap<String, Integer> map =
        sequencedCollectionFeatures.insertAtBothEnds("a", 1, "z", 9);
    Map.Entry<String, Integer> entry = sequencedCollectionFeatures.firstEntry(map);
    assertEquals("a", entry.getKey());
    assertEquals(Integer.valueOf(1), entry.getValue());
  }

  @Test
  public void testLastEntry() {
    SequencedMap<String, Integer> map =
        sequencedCollectionFeatures.insertAtBothEnds("a", 1, "z", 9);
    Map.Entry<String, Integer> entry = sequencedCollectionFeatures.lastEntry(map);
    assertEquals("z", entry.getKey());
    assertEquals(Integer.valueOf(9), entry.getValue());
  }
}
