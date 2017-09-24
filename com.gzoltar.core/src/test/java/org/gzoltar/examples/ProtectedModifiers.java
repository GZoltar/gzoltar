package org.gzoltar.examples;

public class ProtectedModifiers {

  protected String string = null;

  protected boolean isNegative(int x) {
    if (x >= 0) {
      return false;
    } else {
      return true;
    }
  }

}
