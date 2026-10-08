package org.gzoltar.java11;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Exercises Predicate.not(Predicate), a static factory method added in Java 11 for cleanly
 * negating an existing predicate or method reference such as String::isBlank, which has no
 * built-in negated form the way a boolean expression would.
 */
public class PredicateFeatures {

  public List<String> nonBlankValues(List<String> values) {
    return values.stream()
        .filter(Predicate.not(String::isBlank))
        .collect(Collectors.toList());
  }

  public List<Integer> nonNegativeValues(List<Integer> numbers) {
    Predicate<Integer> isNegative = n -> n < 0;
    return numbers.stream()
        .filter(Predicate.not(isNegative))
        .collect(Collectors.toList());
  }

}
