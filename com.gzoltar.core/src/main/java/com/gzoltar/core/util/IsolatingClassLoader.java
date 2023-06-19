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

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A classloader that should have the same classpath as the "normal" classloader, but shares
 * absolutely nothing with it. Used to ensure that the execution of a single test method doest not
 * change the static state of each loaded class, which will affect later the execution of other test
 * methods.
 */
public class IsolatingClassLoader extends URLClassLoader {

  public IsolatingClassLoader(final URL[] classpath, final ClassLoader parent) {
    super(classpath, parent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected synchronized Class<?> loadClass(final String name, final boolean resolve)
      throws ClassNotFoundException {

    // TODO Why do we need to load these packages with the parent classloader?
    if (name.startsWith("junit.") || name.startsWith("org.junit.")
        || name.startsWith("org.hamcrest.")) {
      return super.loadClass(name, resolve);
    }

    // Load class from this classloader
    Class<?> clazz = this.findLoadedClass(name);
    if (clazz == null) {
      try {
        // Not loaded yet, try to find it in this classloader
        clazz = this.findClass(name);
      } catch (ClassNotFoundException e) {
        // Not found in this classloader, try to find it in parent classloaders
        clazz = super.loadClass(name, resolve);
      }
    }
    if (resolve) {
      this.resolveClass(clazz);
    }
    return clazz;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized Enumeration<URL> getResources(final String name) throws IOException {
    final List<URL> allResources = new LinkedList<>();

    // Load resource from this classloader
    Enumeration<URL> thisResources = this.findResources(name);
    if (thisResources != null) {
      while (thisResources.hasMoreElements()) {
        allResources.add(thisResources.nextElement());
      }
    }

    // Then try finding resources from parent classloaders
    Enumeration<URL> parentResources = super.findResources(name);
    if (parentResources != null) {
      while (parentResources.hasMoreElements()) {
        allResources.add(parentResources.nextElement());
      }
    }

    return new Enumeration<URL>() {
      final Iterator<URL> it = allResources.iterator();

      @Override
      public boolean hasMoreElements() {
        return it.hasNext();
      }

      @Override
      public URL nextElement() {
        return it.next();
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized URL getResource(final String name) {
    URL resource = this.findResource(name);
    if (resource == null) {
      resource = super.getResource(name);
    }
    return resource;
  }

}
