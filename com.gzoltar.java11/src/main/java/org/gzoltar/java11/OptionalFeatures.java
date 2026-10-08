package org.gzoltar.java11;

import java.util.Optional;

/**
 * Exercises Optional#isEmpty() (Java 11) -- the readability-friendly counterpart to isPresent(),
 * letting you write `if (optional.isEmpty())` instead of `if (!optional.isPresent())`.
 * (Optional#stream(), added in Java 9, is intentionally left out of this Java 11-only project.)
 */
public class OptionalFeatures {

  public boolean isEmpty(Optional<String> value) {
    return value.isEmpty();
  }

}
