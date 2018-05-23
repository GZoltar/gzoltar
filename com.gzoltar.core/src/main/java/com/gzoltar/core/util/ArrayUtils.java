package com.gzoltar.core.util;

public final class ArrayUtils {

  /**
   * Checks whether a boolean array contains a specific boolean value.
   * 
   * @param arr boolean array
   * @param value boolean value
   * @return <code>true</code> if the boolean array contains the boolean value, <code>false</code>
   *         otherwise.
   */
  public static boolean containsValue(boolean[] arr, boolean value) {
    if (arr == null) {
      return false;
    }

    for (int i = 0; i < arr.length; i++) {
      if (arr[i] == value) {
        return true;
      }
    }

    return false;
  }

}
