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
