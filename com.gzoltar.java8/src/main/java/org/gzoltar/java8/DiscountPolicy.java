package org.gzoltar.java8;

/**
 * Exercises two Java 8 interface features: static methods and default methods. Both live in the
 * interface's own class file, which is interesting for coverage tools since, before Java 8,
 * interfaces could never contain executable bytecode.
 */
public interface DiscountPolicy {

  double STANDARD_TAX_RATE = 0.18;

  /** Static method on an interface. */
  static double standardTaxRate() {
    return STANDARD_TAX_RATE;
  }

  /** Default method on an interface. */
  default double applyDiscount(double price, double discountPercentage) {
    if (discountPercentage < 0 || discountPercentage > 100) {
      throw new IllegalArgumentException("discountPercentage must be between 0 and 100");
    }
    return price - (price * discountPercentage / 100.0);
  }

  /** A second default method, calling the static one. */
  default double applyTax(double price) {
    return price + (price * standardTaxRate());
  }

}
