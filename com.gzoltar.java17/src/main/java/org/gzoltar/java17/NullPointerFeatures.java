package org.gzoltar.java17;

/**
 * Exercises Helpful NullPointerExceptions (JEP 358, on by default since Java 15): when a chained
 * call throws an NPE, the message now names exactly which part of the expression was null,
 * instead of the old bare "NullPointerException" with no detail at all.
 */
public class NullPointerFeatures {

  public static class Box {
    private Inner inner;

    public Inner getInner() {
      return inner;
    }
  }

  public static class Inner {
    private String value;

    public String getValue() {
      return value;
    }
  }

  public String helpfulMessageFor(Box box) {
    try {
      box.getInner().getValue().trim();
      return null;
    } catch (NullPointerException e) {
      return e.getMessage();
    }
  }

}
