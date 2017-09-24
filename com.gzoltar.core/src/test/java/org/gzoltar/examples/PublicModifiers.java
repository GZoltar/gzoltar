package org.gzoltar.examples;

public class PublicModifiers extends AbstractClass {

  public String string = null;

  @Override
  public boolean isNegative(int x) {
    if (x >= 0) {
      return false;
    } else {
      return true;
    }
  }

}
