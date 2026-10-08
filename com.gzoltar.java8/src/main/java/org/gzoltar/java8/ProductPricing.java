package org.gzoltar.java8;

import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Consumes the default/static methods from {@link DiscountPolicy} and adds a method-reference
 * based aggregation over a list of {@link Product}s.
 */
public class ProductPricing implements DiscountPolicy {

  public double calculateTotalWithTax(double price) {
    return applyTax(price);
  }

  public double calculateDiscountedTotal(double price, double discountPercentage) {
    double discounted = applyDiscount(price, discountPercentage);
    return applyTax(discounted);
  }

  public double sumPrices(List<Product> products) {
    ToDoubleFunction<Product> toPrice = Product::getPrice; // method reference
    return products.stream().mapToDouble(toPrice).sum();
  }

}
