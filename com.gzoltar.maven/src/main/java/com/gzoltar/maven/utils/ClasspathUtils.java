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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import org.apache.maven.project.MavenProject;

public final class ClasspathUtils {

  public static URL[] getTestClasspath(final MavenProject mavenProject) throws Exception {
    List<String> elements = mavenProject.getTestClasspathElements();
    URL[] urls = new URL[elements.size()];
    for (int i = 0; i < elements.size(); i++) {
      urls[i] = new File(elements.get(i)).toURI().toURL();
    }
    return urls;
  }

  public static void setClassLoaderClasspath(final URL[] classpath) {
    ClassLoader contextClassLoader =
        URLClassLoader.newInstance(classpath, Thread.currentThread().getContextClassLoader());
    Thread.currentThread().setContextClassLoader(contextClassLoader);
  }
}
