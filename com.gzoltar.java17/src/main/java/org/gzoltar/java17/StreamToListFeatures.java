package org.gzoltar.java17;

import java.util.List;

/**
 * Exercises Stream#toList() (added in Java 16): a shorter alternative to
 * `.collect(Collectors.toList())` that also returns an unmodifiable list.
 */
public class StreamToListFeatures {

  public List<Integer> squares(List<Integer> numbers) {
    return numbers.stream()
        .map(n -> n * n)
        .toList();
  }

  public boolean isImmutable(List<Integer> list) {
    try {
      list.add(0);
      return false;
    } catch (UnsupportedOperationException e) {
      return true;
    }
  }

}
