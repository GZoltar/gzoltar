/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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
