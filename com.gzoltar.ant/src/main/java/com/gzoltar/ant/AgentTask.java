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

import org.apache.tools.ant.BuildException;

/**
 * Ant task that will unpack the coverage agent jar and generate the JVM options required to use it.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's ant module for convenience.
 */
public class AgentTask extends AbstractCoverageTask {

  private String property;

  /**
   * Sets the name of the property to hold the agent JVM options
   * 
   * @param property Name of the property to be populated
   */
  public void setProperty(final String property) {
    this.property = property;
  }

  /**
   * Unpacks a private copy of the GZoltar agent and populates <code>property</code> with the JVM
   * arguments required to use it. The value set into the property is only valid for the lifetime of
   * the current JVM. The agent jar will be removed on termination of the JVM.
   */
  @Override
  public void execute() throws BuildException {
    if (this.property == null || this.property.length() == 0) {
      throw new BuildException("Property is mandatory", getLocation());
    }

    final String jvmArg = this.isEnabled() ? this.getLaunchingArgument() : "";
    getProject().setNewProperty(this.property, jvmArg);
  }
}
