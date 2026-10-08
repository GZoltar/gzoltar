package org.gzoltar.java11;

/**
 * Simple immutable POJO used across the Java 11 examples.
 */
public class Product {

  private final String name;

  private final double price;

  public Product(String name, double price) {
    this.name = name;
    this.price = price;
  }

  public String getName() {
    return this.name;
  }

  public double getPrice() {
    return this.price;
  }

  @Override
  public String toString() {
    return this.name + ":" + this.price;
  }

}
