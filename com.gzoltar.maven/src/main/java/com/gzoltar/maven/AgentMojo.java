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
package com.gzoltar.maven;

import java.io.File;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

/**
 * Prepares a property pointing to the GZoltar runtime agent that can be passed as a VM argument to
 * the application under test.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's maven module for convenience.
 */
@Mojo(name = "prepare-agent", defaultPhase = LifecyclePhase.INITIALIZE,
    requiresDependencyResolution = ResolutionScope.RUNTIME, threadSafe = true)
public class AgentMojo extends AbstractAgentMojo {

  /**
   * Path to the output file for execution data.
   */
  @Parameter(property = "gzoltar.destFile",
      defaultValue = "${project.build.directory}/gzoltar.ser")
  private File destFile;

  /**
   * @return the destFile
   */
  @Override
  protected File getDestFile() {
    return this.destFile;
  }
}
