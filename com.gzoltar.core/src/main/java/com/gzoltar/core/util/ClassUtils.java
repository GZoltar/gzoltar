/**
 * Copyright (C) 2018 GZoltar contributors.
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
import javassist.bytecode.ClassFile;

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
    return Character.isDigit(clazz.getName().charAt(pos + 1));
  }

  /**
   * Returns <code>true</code> if a {@link javassist.CtClass} object is a Java interface and its
   * version is 52 or above (i.e., >= Java 1.8), false otherwise.
   * 
   * @param ctClass
   * @return
   */
  public static boolean isInterfaceClassSupported(final CtClass ctClass) {
    return ctClass.isInterface() && ctClass.getClassFile().getMajorVersion() >= ClassFile.JAVA_8;
  }
}
