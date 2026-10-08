package org.gzoltar.java17;

/**
 * Exercises Sealed Classes (JEP 409, finalized in Java 17): a sealed interface restricts which
 * classes are permitted to implement it, giving the compiler (and readers) a closed, exhaustive
 * set of subtypes to reason about.
 */
public class SealedFeatures {

  public sealed interface Shape permits Circle, Rectangle {
  }

  public record Circle(double radius) implements Shape {
  }

  public record Rectangle(double width, double height) implements Shape {
  }

  public double area(Shape shape) {
    if (shape instanceof Circle) {
      Circle c = (Circle) shape;
      return Math.PI * c.radius() * c.radius();
    } else if (shape instanceof Rectangle) {
      Rectangle r = (Rectangle) shape;
      return r.width() * r.height();
    }
    throw new IllegalStateException("Unknown shape: " + shape);
  }

}
