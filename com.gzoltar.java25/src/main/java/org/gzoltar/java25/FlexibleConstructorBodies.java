package org.gzoltar.java25;

/**
 * Flexible Constructor Bodies (JEP 513, finalized in Java 25).
 *
 * Before Java 25, an explicit this(...)/super(...) call had to be the very
 * first statement in a constructor, so it was impossible to validate
 * arguments *before* delegating to a superclass constructor that takes those
 * same arguments. Now a constructor may have a "prologue" of statements
 * before the explicit super()/this() call, as long as those statements do
 * not read the instance being constructed (no access to fields, instance
 * methods, or `this`).
 */
public class FlexibleConstructorBodies {

  public static class NonNegative {
    protected final int value;

    public NonNegative(int value) {
      this.value = value;
    }
  }

  /** Requires a strictly positive value, validated in the constructor's prologue. */
  public static class StrictlyPositive extends NonNegative {
    public StrictlyPositive(int value) {
      if (value < 0) { // FAULT: should be value <= 0, so that 0 is also rejected
        throw new IllegalArgumentException("value must be strictly positive");
      }
      super(value); // the prologue above now runs before this explicit super() call
    }
  }

  public int createValid(int value) {
    return new StrictlyPositive(value).value;
  }

  public boolean rejectsNonPositive(int value) {
    try {
      new StrictlyPositive(value);
      return false;
    } catch (IllegalArgumentException e) {
      return true;
    }
  }

}
