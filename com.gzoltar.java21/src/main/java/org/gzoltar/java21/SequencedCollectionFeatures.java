package org.gzoltar.java21;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.SequencedMap;

/**
 * Sequenced Collections (JEP 431, finalized/standard in Java 21).
 *
 * Adds a common supertype (SequencedCollection / SequencedMap / SequencedSet)
 * with well-defined encounter order, plus getFirst()/getLast()/reversed() and
 * (for maps) putFirst()/putLast()/firstEntry()/lastEntry().
 */
public class SequencedCollectionFeatures {

  public int first(List<Integer> list) {
    return list.getFirst();
  }

  public int last(List<Integer> list) {
    return list.getLast();
  }

  public List<Integer> reversedView(List<Integer> list) {
    return list.reversed();
  }

  /**
   * Returns the second-to-last element.
   *
   * NOTE: contains an intentional FAULT. It reads index 0 of the reversed
   * view (i.e. the last element again) instead of index 1 (the actual
   * second-to-last element).
   */
  public int secondToLast(List<Integer> list) {
    SequencedCollection<Integer> reversed = list.reversed();
    return reversed.stream().skip(0).findFirst().orElseThrow(); // FAULT: skip(0) should be skip(1)
  }

  public SequencedMap<String, Integer> insertAtBothEnds(String firstKey, int firstValue,
      String lastKey, int lastValue) {
    SequencedMap<String, Integer> map = new LinkedHashMap<>();
    map.put("middle", 0);
    map.putFirst(firstKey, firstValue);
    map.putLast(lastKey, lastValue);
    return map;
  }

  public Map.Entry<String, Integer> firstEntry(SequencedMap<String, Integer> map) {
    return map.firstEntry();
  }

  public Map.Entry<String, Integer> lastEntry(SequencedMap<String, Integer> map) {
    return map.lastEntry();
  }
}
