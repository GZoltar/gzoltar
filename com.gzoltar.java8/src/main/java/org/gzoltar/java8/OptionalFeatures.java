package org.gzoltar.java8;

import java.util.List;
import java.util.Optional;

/**
 * Exercises java.util.Optional: of/empty, map, filter, orElse, orElseGet and orElseThrow.
 */
public class OptionalFeatures {

  public Optional<Product> findByName(List<Product> products, String name) {
    for (Product p : products) {
      if (p.getName().equals(name)) {
        return Optional.of(p);
      }
    }
    return Optional.empty();
  }

  public double priceOrDefault(List<Product> products, String name, double defaultPrice) {
    return findByName(products, name)
        .map(Product::getPrice)
        .orElse(defaultPrice);
  }

  public String describe(Optional<Product> product) {
    return product
        .filter(p -> p.getPrice() > 0)
        .map(p -> p.getName() + " costs " + p.getPrice())
        .orElseGet(() -> "no product available");
  }

  public Product requireProduct(Optional<Product> product) {
    return product.orElseThrow(() -> new IllegalStateException("product not found"));
  }

}
