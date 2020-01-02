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
package com.gzoltar.cli.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import com.gzoltar.cli.Command;

/**
 * The <code>version</code> command.
 */
public class Version extends Command {

  @Override
  public String description() {
    return "Print GZoltar version information.";
  }

  @Override
  public int execute(final PrintStream out, final PrintStream err) throws IOException {
    // Find the path of the compiled class
    String classPath =
        Version.class.getResource(Version.class.getSimpleName() + ".class").toString();

    // Find the path of the lib which includes the class
    String libPath = classPath.substring(0, classPath.lastIndexOf("!"));

    // Find the path of the file inside the lib jar
    String filePath = libPath + "!/META-INF/MANIFEST.MF";

    // Look at the manifest file and get the 'Implementation-Version' attribute
    Manifest manifest = new Manifest(new URL(filePath).openStream());
    Attributes attr = manifest.getMainAttributes();
    out.println("v" + attr.getValue("Implementation-Version"));

    return 0;
  }
}
