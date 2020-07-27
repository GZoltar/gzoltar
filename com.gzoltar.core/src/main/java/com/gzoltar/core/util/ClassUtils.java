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

import javassist.CtClass;

public class ClassUtils {

  /**
   * 
   * @param clazz
   * @return
   */
  public static boolean isAnonymousClass(CtClass clazz) {
    // return cc.getClassFile2().getInnerAccessFlags() == 8;
    int pos = clazz.getName().lastIndexOf('$');
    if (pos < 0) {
      return false;
    }
    return pos == clazz.getName().length() - 1
        || Character.isDigit(clazz.getName().charAt(pos + 1));
  }
}
