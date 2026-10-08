package org.gzoltar.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Exercises lambda expressions, core functional interfaces (Predicate, Function, BiFunction,
 * Supplier) and all four kinds of method references. Lambdas compile down to invokedynamic /
 * synthetic lambda$ methods, which is the bytecode shape most likely to trip up a coverage tool.
 */
public class LambdaFeatures {

  public List<Integer> filterList(List<Integer> numbers, Predicate<Integer> predicate) {
    if (numbers == null) {
      return new ArrayList<>();
    }
    List<Integer> result = new ArrayList<>();
    for (Integer n : numbers) {
      if (predicate.test(n)) {
        result.add(n);
      }
    }
    return result;
  }

  public <T, R> List<R> transformList(List<T> values, Function<T, R> mapper) {
    List<R> result = new ArrayList<>();
    if (values == null) {
      return result;
    }
    for (T value : values) {
      result.add(mapper.apply(value));
    }
    return result;
  }

  public int combine(int a, int b, BiFunction<Integer, Integer, Integer> operation) {
    return operation.apply(a, b);
  }

  public String supplyGreeting(Supplier<String> supplier) {
    return supplier.get();
  }

  /** Method reference to an instance method of an arbitrary object of a particular type. */
  public String toUpperCase(String value) {
    Function<String, String> ref = String::toUpperCase;
    return ref.apply(value);
  }

  /** Method reference to a static method. */
  public Integer parse(String value) {
    Function<String, Integer> ref = Integer::parseInt;
    return ref.apply(value);
  }

  /** Method reference to a constructor. */
  public Supplier<StringBuilder> builderSupplier() {
    return StringBuilder::new;
  }

}
