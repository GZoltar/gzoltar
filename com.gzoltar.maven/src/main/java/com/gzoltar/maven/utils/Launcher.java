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
package com.gzoltar.maven.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.gzoltar.core.util.SystemProperties;

public final class Launcher {

  public static int launch(final List<String> args) throws Exception {
    List<String> commandLineArgs = new ArrayList<String>();
    if (SystemProperties.OS_NAME.contains("windows") == true) {
      commandLineArgs.add(SystemProperties.JAVA_HOME + ".exe");
    } else {
      commandLineArgs.add(SystemProperties.JAVA_HOME);
    }

    assert commandLineArgs.size() != 0;
    commandLineArgs.addAll(args);

    ProcessBuilder pb = new ProcessBuilder(commandLineArgs);
    pb.redirectErrorStream(true);;
    final Process p = pb.start();

    InputStream is = p.getInputStream();
    BufferedInputStream isl = new BufferedInputStream(is);
    byte buffer[] = new byte[1024];
    int len = 0;
    while ((len = isl.read(buffer)) != -1) {
      System.out.write(buffer, 0, len);
    }

    int i = p.waitFor();
    if (i == 0) {
      return 0;
    }

    return 1;
  }
}
