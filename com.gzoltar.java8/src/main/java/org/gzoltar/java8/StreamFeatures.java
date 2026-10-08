package org.gzoltar.java8;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Exercises the Stream API and java.util.stream.Collectors: filter/map/collect, joining,
 * partitioningBy, groupingBy, primitive IntStream, and stream max() returning an Optional.
 */
public class StreamFeatures {

  public List<Integer> squaresOfEvens(List<Integer> numbers) {
    return numbers.stream()
        .filter(n -> n % 2 == 0)
        .map(n -> n * n)
        .collect(Collectors.toList());
  }

  public String joinNames(List<String> names) {
    return names.stream()
        .filter(name -> !name.isEmpty())
        .sorted()
        .collect(Collectors.joining(", "));
  }

  public long countExpensiveProducts(List<Product> products, double threshold) {
    return products.stream()
        .filter(p -> p.getPrice() >= threshold) /* FAULT: should be strictly greater than */
        .count();
  }

  public Map<Boolean, List<Product>> partitionByPrice(List<Product> products, double threshold) {
    return products.stream()
        .collect(Collectors.partitioningBy(p -> p.getPrice() > threshold));
  }

  public Map<String, List<Product>> groupByName(List<Product> products) {
    return products.stream()
        .collect(Collectors.groupingBy(Product::getName));
  }

  public double sumOfFirstNSquares(int n) {
    return IntStream.rangeClosed(1, n)
        .mapToDouble(i -> i * i)
        .sum();
  }

  public Optional<Product> mostExpensive(List<Product> products) {
    return products.stream().max(Comparator.comparingDouble(Product::getPrice));
  }

}
