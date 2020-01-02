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

public final class SystemProperties {

  public static final String PATH_SEPARATOR = System.getProperty("path.separator");
  public static final String FILE_SEPARATOR = System.getProperty("file.separator");
  public static final String JAVA_HOME = System.getProperty("java.home")
      + SystemProperties.FILE_SEPARATOR + "bin" + SystemProperties.FILE_SEPARATOR + "java";

  public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

  /**
   * 
   * @return
   */
  public static String getClasspathString() {
    return System.getProperty("java.class.path");
  }

  /**
   * 
   * @return
   */
  public static String[] getClasspathArray() {
    return System.getProperty("java.class.path").split(SystemProperties.PATH_SEPARATOR);
  }
}
