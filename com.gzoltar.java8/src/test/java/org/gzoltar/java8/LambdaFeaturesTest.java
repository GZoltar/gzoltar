package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class LambdaFeaturesTest {

  private final LambdaFeatures lambdaFeatures = new LambdaFeatures();

  @Test
  public void testFilterList_EvenNumbers() {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
    List<Integer> evens = lambdaFeatures.filterList(numbers, n -> n % 2 == 0);
    assertEquals(Arrays.asList(2, 4, 6), evens);
  }

  @Test
  public void testFilterList_NullInput() {
    List<Integer> result = lambdaFeatures.filterList(null, n -> true);
    assertTrue(result.isEmpty());
  }

  @Test
  public void testTransformList_LengthOfWords() {
    List<String> words = Arrays.asList("gzoltar", "java", "lambda");
    List<Integer> lengths = lambdaFeatures.transformList(words, String::length);
    assertEquals(Arrays.asList(7, 4, 6), lengths);
  }

  @Test
  public void testCombine_Sum() {
    int result = lambdaFeatures.combine(3, 4, (a, b) -> a + b);
    assertEquals(7, result);
  }

  @Test
  public void testSupplyGreeting() {
    String greeting = lambdaFeatures.supplyGreeting(() -> "hello gzoltar");
    assertEquals("hello gzoltar", greeting);
  }

  @Test
  public void testToUpperCase_MethodReference() {
    assertEquals("GZOLTAR", lambdaFeatures.toUpperCase("gzoltar"));
  }

  @Test
  public void testParse_StaticMethodReference() {
    assertEquals(Integer.valueOf(42), lambdaFeatures.parse("42"));
  }

  @Test
  public void testBuilderSupplier_ConstructorReference() {
    StringBuilder builder = lambdaFeatures.builderSupplier().get();
    builder.append("gzoltar");
    assertEquals("gzoltar", builder.toString());
  }

}
