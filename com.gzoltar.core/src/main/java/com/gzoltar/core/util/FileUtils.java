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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class FileUtils {

  /**
   * Find all files in a directory that satisfy a particular extension.
   * 
   * @param dir top-level directory to search for
   * @param ext extension to search for
   * @param recursively if <code>true</code> search is performed recursively
   * @return a {@link java.util.List} of all files found
   */
  public static List<File> listFiles(final File dir, final String ext, final boolean recursively) {
    List<File> files = new ArrayList<File>();

    for (File file : dir.listFiles()) {
      if (file.isDirectory() && recursively) {
        files.addAll(listFiles(file, ext, recursively));
      } else if (file.isFile() && file.getName().endsWith(ext)) {
        files.add(file);
      }
    }

    return files;
  }

}
