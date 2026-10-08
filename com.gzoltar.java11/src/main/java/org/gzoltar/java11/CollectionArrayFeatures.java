package org.gzoltar.java11;

import java.util.List;

/**
 * Exercises Collection#toArray(IntFunction) added in Java 11 -- a type-safe alternative to the
 * classic `list.toArray(new String[0])` idiom that lets the JIT skip guessing/resizing the array.
 */
public class CollectionArrayFeatures {

  public String[] toStringArray(List<String> values) {
    return values.toArray(String[]::new);
  }

  public int totalLength(List<String> values) {
    String[] array = toStringArray(values);
    int total = 0;
    for (String value : array) {
      total += value.length();
    }
    return total;
  }

}
