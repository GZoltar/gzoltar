package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Test;

public class StreamFeaturesTest {

  private final StreamFeatures streamFeatures = new StreamFeatures();

  @Test
  public void testSquaresOfEvens() {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
    assertEquals(Arrays.asList(4, 16, 36), streamFeatures.squaresOfEvens(numbers));
  }

  @Test
  public void testJoinNames() {
    List<String> names = Arrays.asList("charlie", "", "alice", "bob");
    assertEquals("alice, bob, charlie", streamFeatures.joinNames(names));
  }

  @Test
  public void testCountExpensiveProducts_ExclusiveThreshold() {
    List<Product> products = Arrays.asList(
        new Product("a", 10.0),
        new Product("b", 20.0),
        new Product("c", 20.0));
    // Only products strictly above the threshold should count; this exposes the injected FAULT.
    assertEquals(0, streamFeatures.countExpensiveProducts(products, 20.0));
  }

  @Test
  public void testCountExpensiveProducts_AboveThreshold() {
    List<Product> products = Arrays.asList(
        new Product("a", 10.0),
        new Product("b", 30.0));
    assertEquals(1, streamFeatures.countExpensiveProducts(products, 20.0));
  }

  @Test
  public void testPartitionByPrice() {
    List<Product> products = Arrays.asList(
        new Product("a", 5.0),
        new Product("b", 50.0));
    Map<Boolean, List<Product>> partitions = streamFeatures.partitionByPrice(products, 10.0);
    assertEquals(1, partitions.get(true).size());
    assertEquals(1, partitions.get(false).size());
  }

  @Test
  public void testGroupByName() {
    List<Product> products = Arrays.asList(
        new Product("apple", 1.0),
        new Product("apple", 1.5),
        new Product("pear", 2.0));
    Map<String, List<Product>> grouped = streamFeatures.groupByName(products);
    assertEquals(2, grouped.get("apple").size());
    assertEquals(1, grouped.get("pear").size());
  }

  @Test
  public void testSumOfFirstNSquares() {
    assertEquals(30.0, streamFeatures.sumOfFirstNSquares(4), 0.0001);
  }

  @Test
  public void testMostExpensive() {
    List<Product> products = Arrays.asList(
        new Product("a", 10.0),
        new Product("b", 99.0));
    Optional<Product> result = streamFeatures.mostExpensive(products);
    assertTrue(result.isPresent());
    assertEquals("b", result.get().getName());
  }

}
