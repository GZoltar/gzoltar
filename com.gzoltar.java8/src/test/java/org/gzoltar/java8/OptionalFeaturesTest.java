package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class OptionalFeaturesTest {

  private final OptionalFeatures optionalFeatures = new OptionalFeatures();

  private final List<Product> products = Arrays.asList(
      new Product("gzoltar", 0.0),
      new Product("junit", 15.0));

  @Test
  public void testFindByName_Present() {
    Optional<Product> result = optionalFeatures.findByName(products, "junit");
    assertTrue(result.isPresent());
    assertEquals(15.0, result.get().getPrice(), 0.0001);
  }

  @Test
  public void testFindByName_Absent() {
    Optional<Product> result = optionalFeatures.findByName(products, "maven");
    assertFalse(result.isPresent());
  }

  @Test
  public void testPriceOrDefault_Found() {
    assertEquals(15.0, optionalFeatures.priceOrDefault(products, "junit", -1.0), 0.0001);
  }

  @Test
  public void testPriceOrDefault_NotFound() {
    assertEquals(-1.0, optionalFeatures.priceOrDefault(products, "maven", -1.0), 0.0001);
  }

  @Test
  public void testDescribe_FreeProductFilteredOut() {
    Optional<Product> free = optionalFeatures.findByName(products, "gzoltar");
    assertEquals("no product available", optionalFeatures.describe(free));
  }

  @Test
  public void testDescribe_PaidProduct() {
    Optional<Product> paid = optionalFeatures.findByName(products, "junit");
    assertEquals("junit costs 15.0", optionalFeatures.describe(paid));
  }

  @Test(expected = IllegalStateException.class)
  public void testRequireProduct_ThrowsWhenAbsent() {
    optionalFeatures.requireProduct(Optional.empty());
  }

}
