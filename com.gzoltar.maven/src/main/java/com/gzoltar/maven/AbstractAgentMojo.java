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
import java.util.Map;
import java.util.Properties;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import com.gzoltar.core.AgentConfigs;

/**
 * Base class for preparing a property pointing to the GZoltar runtime agent that can be passed as a
 * VM argument to the application under test.
 * 
 * DISCLAIMER: this class has been exported from JaCoCo's maven module for convenience.
 */
public abstract class AbstractAgentMojo extends AbstractGZoltarMojo {

  /**
   * Name of the GZoltar Agent artifact.
   */
  private static final String AGENT_ARTIFACT_NAME = "com.gzoltar:com.gzoltar.agent";

  /**
   * Name of the property used in maven-surefire-plugin.
   */
  private static final String SUREFIRE_ARG_LINE = "argLine";

  /**
   * Map of plugin artifacts.
   */
  @Parameter(property = "plugin.artifactMap", required = true, readonly = true)
  private Map<String, Artifact> pluginArtifactMap;

  /**
   * A list of class loader names, that should be excluded from execution analysis. The list entries
   * are separated by a colon (:) and may use wildcard characters (* and ?). This option might be
   * required in case of special frameworks that conflict with GZoltar code instrumentation, in
   * particular class loaders that do not have access to the Java runtime classes.
   */
  @Parameter(property = "gzoltar.exclClassLoaders", defaultValue = "sun.reflect.DelegatingClassLoader")
  private String exclClassLoaders;

  /**
   * Specifies whether classes without source location should be instrumented.
   */
  @Parameter(property = "gzoltar.inclNoLocationClasses", defaultValue = "false")
  private Boolean inclNoLocationClasses;

  /**
   * Output method to use for writing coverage data. Valid options are:
   * <ul>
   * <li>file: At VM termination execution data is written to a file (default).</li>
   * <li>console: At VM termination execution data is written to the stdout.</li>
   * <li>none: Do not produce any output.</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.output", defaultValue = "FILE")
  private String output;

  @Override
  public void executeMojo() throws MojoExecutionException, MojoFailureException {
    final Properties projectProperties = this.getProject().getProperties();
    final String oldValue = projectProperties.getProperty(SUREFIRE_ARG_LINE);
    final String newValue =
        createAgentConfigurations().prependVMArguments(oldValue, getAgentJarFile());
    getLog().info(SUREFIRE_ARG_LINE + " set to " + newValue);
    projectProperties.setProperty(SUREFIRE_ARG_LINE, newValue);
  }

  /**
   * Return a string representation of the GZoltar Java agent with all properties defined.
   * 
   * @return
   */
  public String prepareAgentVM() {
    return this.createAgentConfigurations().prependVMArguments("", getAgentJarFile());
  }

  private File getAgentJarFile() {
    final Artifact gzoltarAgentArtifact = this.pluginArtifactMap.get(AGENT_ARTIFACT_NAME);
    return gzoltarAgentArtifact.getFile();
  }

  protected AgentConfigs createAgentConfigurations() {
    final AgentConfigs agentConfigs = new AgentConfigs();

    String targetClassesDirectory = this.getProject().getBuild().getOutputDirectory();
    agentConfigs.setBuildLocation(targetClassesDirectory);
    if (this.getDestFile() != null) {
      agentConfigs.setDestfile(this.getDestFile().getAbsolutePath());
    }

    if (this.getIncludes() != null && !this.getIncludes().isEmpty()) {
      final String agentIncludes = StringUtils.join(this.getIncludes().iterator(), ":");
      agentConfigs.setIncludes(agentIncludes);
    }

    if (this.getExcludes() != null && !this.getExcludes().isEmpty()) {
      final String agentExcludes = StringUtils.join(this.getExcludes().iterator(), ":");
      agentConfigs.setIncludes(agentExcludes);
    }

    if (this.exclClassLoaders != null) {
      agentConfigs.setExclClassloader(this.exclClassLoaders);
    }

    if (this.inclNoLocationClasses != null) {
      agentConfigs.setInclNoLocationClasses(this.inclNoLocationClasses.booleanValue());
    }

    if (this.output != null) {
      agentConfigs.setOutput(this.output);
    }

    return agentConfigs;
  }

  public String getOutput() {
    return this.output;
  }

  /**
   * @return the destFile
   */
  protected abstract File getDestFile();
}
