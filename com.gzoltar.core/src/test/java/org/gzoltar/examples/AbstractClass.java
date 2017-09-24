package org.gzoltar.examples;

public abstract class AbstractClass {

  public abstract boolean isNegative(int x);

  public String toString() {
    return this.getClass().getCanonicalName();
  }

}
