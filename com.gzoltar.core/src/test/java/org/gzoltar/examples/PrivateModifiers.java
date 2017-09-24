package org.gzoltar.examples;

@SuppressWarnings("unused")
public class PrivateModifiers {

  private String string = null;

  private boolean isNegative(int x) {
    if (x >= 0) {
      return false;
    } else {
      return true;
    }
  }

}
