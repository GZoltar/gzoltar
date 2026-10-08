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
import javassist.NotFoundException;

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

  /**
   * Checks whether a class is a Java record (Java 16+). Every record implicitly extends
   * java.lang.Record, in the same way that every enum implicitly extends java.lang.Enum.
   *
   * @param clazz a class
   * @return true if {@code clazz} is a record, false otherwise
   */
  public static boolean isRecord(CtClass clazz) {
    try {
      CtClass superclass = clazz.getSuperclass();
      return superclass != null && "java.lang.Record".equals(superclass.getName());
    } catch (NotFoundException e) {
      return false;
    }
  }
}
