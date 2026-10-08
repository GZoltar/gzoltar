package org.gzoltar.java8;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ProductPricingTest {

  private final ProductPricing productPricing = new ProductPricing();

  @Test
  public void testStandardTaxRate_StaticInterfaceMethod() {
    assertEquals(0.18, DiscountPolicy.standardTaxRate(), 0.0001);
  }

  @Test
  public void testCalculateTotalWithTax() {
    assertEquals(118.0, productPricing.calculateTotalWithTax(100.0), 0.0001);
  }

  @Test
  public void testCalculateDiscountedTotal() {
    // 100 - 10% = 90, then +18% tax = 106.2
    assertEquals(106.2, productPricing.calculateDiscountedTotal(100.0, 10.0), 0.0001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyDiscount_InvalidPercentage() {
    productPricing.calculateDiscountedTotal(100.0, 150.0);
  }

  @Test
  public void testSumPrices_MethodReference() {
    List<Product> products = Arrays.asList(new Product("a", 10.0), new Product("b", 15.5));
    assertEquals(25.5, productPricing.sumPrices(products), 0.0001);
  }

}
