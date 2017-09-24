package org.gzoltar.examples;

public final class PublicFinalModifiers {

  public final String string = null;

  public final boolean isNegative(int x) {
    if (x >= 0) {
      return false;
    } else {
      return true;
    }
  }

}
