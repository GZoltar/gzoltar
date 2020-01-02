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
package com.gzoltar.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;
import com.gzoltar.core.instr.InstrumentationLevel;
import com.gzoltar.core.instr.Instrumenter;
import javassist.ClassPool;
import javassist.NotFoundException;

/**
 * Task for offline instrumentation of class files.
 */
public class InstrumentTask extends AbstractCoverageTask {

  private File destdir = null;

  private boolean removesignatures = true;

  private final Union files = new Union();

  private Path classpath = null;

  /**
   * Sets the location of the instrumented classes.
   * 
   * @param destdir destination folder for instrumented classes
   */
  public void setDestdir(final File destdir) {
    this.destdir = destdir;
  }

  /**
   * Sets whether signatures should be removed from JAR files.
   * 
   * @param removesignatures <code>true</code> if signatures should be removed
   */
  public void setRemovesignatures(final boolean removesignatures) {
    this.removesignatures = removesignatures;
  }

  /**
   * This task accepts any number of class file resources.
   * 
   * @param resources Execution data resources
   */
  public void addConfigured(final ResourceCollection resources) {
    this.files.add(resources);
  }

  /**
   * Creates a nested classpath element with path/pathelement objects.
   * 
   * @return a {@link org.apache.tools.ant.types.Path} object
   */
  public Path createClasspath() {
    this.classpath = new org.apache.tools.ant.types.Path(getProject());
    return this.classpath;
  }

  private void updateClasspath(final String path) throws BuildException {
    try {
      ClassPool.getDefault().appendClassPath(path);
    } catch (NotFoundException e) {
      throw new BuildException(e);
    }
  }

  /**
   * Executes instrument task
   */
  @Override
  public void execute() throws BuildException {
    log("* Off-line instrumentation of Java class files and jar files");

    if (this.destdir == null) {
      throw new BuildException("Output directory must be supplied", getLocation());
    }

    // configure instrumentation
    this.agentConfigs.setInstrumentationLevel(InstrumentationLevel.OFFLINE);
    Instrumenter instrumenter = new Instrumenter(this.agentConfigs);
    instrumenter.setRemoveSignatures(this.removesignatures);

    // update classpath
    final Iterator<?> resourcePathIterator = this.classpath.iterator();
    while (resourcePathIterator.hasNext()) {
      final Resource resource = (Resource) resourcePathIterator.next();
      this.updateClasspath(resource.toString());
    }

    // instrument
    int numInstrumentedClasses = 0;
    log("* Processing");
    final Iterator<?> resourceIterator = this.files.iterator();
    while (resourceIterator.hasNext()) {
      final Resource source = (Resource) resourceIterator.next();
      if (source.isDirectory()) {
        continue;
      }

      log("  - " + source.getName());
      try {
        numInstrumentedClasses += this.instrument(instrumenter, source);
      } catch (Exception e) {
        throw new BuildException(e);
      }
    }

    log("* " + numInstrumentedClasses + " classes instrumented to "
        + this.destdir.getAbsoluteFile());
    log("* Done!");
  }

  private int instrument(Instrumenter instrumenter, final Resource source) throws Exception {
    final File dest = new File(this.destdir, source.getName());
    dest.getParentFile().mkdirs();

    final InputStream input = source.getInputStream();
    try {
      final OutputStream output = new FileOutputStream(dest);
      try {
        return instrumenter.instrumentToFile(input, output);
      } finally {
        output.close();
      }
    } catch (Exception e) {
      throw e;
    } finally {
      input.close();
    }
  }
}
