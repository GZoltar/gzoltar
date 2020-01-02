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
import java.util.List;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.StringUtils;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.instr.InstrumentationLevel;

/**
 * Base class for creating a report.
 */
public abstract class AbstractReportMojo extends AbstractMavenReport {

  /**
   * Maven project.
   */
  @Parameter(property = "project", readonly = true)
  private MavenProject project;

  /**
   * Output directory for the reports.
   */
  @Parameter(property = "gzoltar.outputDirectory",
      defaultValue = "${project.reporting.outputDirectory}/gzoltar")
  protected File outputDirectory;

  /**
   * File with execution data.
   */
  @Parameter(property = "gzoltar.dataFile",
      defaultValue = "${project.build.directory}/gzoltar.ser")
  protected File dataFile;

  /**
   * A list of class files to include in instrumentation/analysis/reports. May use wildcard
   * characters (* and ?). When not specified everything will be included.
   */
  @Parameter
  private List<String> includes;

  /**
   * A list of class files to exclude from instrumentation/analysis/reports. May use wildcard
   * characters (* and ?). When not specified nothing will be excluded.
   */
  @Parameter
  private List<String> excludes;

  /**
   * Specifies the granularity level. Valid options are:
   * <ul>
   * <li>line (default)</li>
   * <li>method</li>
   * <li>basicblock</li>
   * </ul>
   */
  @Parameter(property = "gzoltar.granularity", defaultValue = "LINE")
  private String granularity;

  /**
   * Specifies whether public methods of each class under test should be included in the report.
   * Default is <code>true</code>.
   */
  @Parameter(property = "gzoltar.inclPublicMethods", defaultValue = "true")
  private Boolean inclPublicMethods;

  /**
   * Specifies whether public static constructors of each class under test should be included in the
   * report. Default is <code>false</code>.
   */
  @Parameter(property = "gzoltar.inclStaticConstructors", defaultValue = "false")
  private Boolean inclStaticConstructors;

  /**
   * Specifies whether methods annotated with @deprecated of each class under test should be
   * included in the report. Default is <code>true</code>.
   */
  @Parameter(property = "gzoltar.inclDeprecatedMethods", defaultValue = "true")
  private Boolean inclDeprecatedMethods;

  @Override
  public String getName(Locale locale) {
    return "GZoltar";
  }

  @Override
  public String getOutputName() {
    return "gzoltar/index";
  }

  @Override
  protected MavenProject getProject() {
    return this.project;
  }

  @Override
  public boolean canGenerateReport() {
    if (!this.dataFile.exists()) {
      getLog().info("Skipping GZoltar execution due to missing execution data file.");
      return false;
    }
    return true;
  }

  @Override
  public void execute() throws MojoExecutionException {
    if (!this.canGenerateReport()) {
      return;
    }

    try {
      this.executeReport(Locale.getDefault());
    } catch (final MavenReportException e) {
      throw new MojoExecutionException(
          "An error has occurred in " + this.getName(Locale.ENGLISH) + " report generation.", e);
    }
  }

  protected AgentConfigs createAgentConfigurations() {
    final AgentConfigs agentConfigs = new AgentConfigs();
    // reports do not require any bytecode injection
    agentConfigs.setInstrumentationLevel(InstrumentationLevel.NONE);

    if (this.includes != null && !this.includes.isEmpty()) {
      final String agentIncludes = StringUtils.join(this.includes.iterator(), ":");
      agentConfigs.setIncludes(agentIncludes);
    }

    if (this.excludes != null && !this.excludes.isEmpty()) {
      final String agentExcludes = StringUtils.join(this.excludes.iterator(), ":");
      agentConfigs.setIncludes(agentExcludes);
    }

    if (this.granularity != null) {
      agentConfigs.setGranularity(this.granularity);
    }

    if (this.inclPublicMethods != null) {
      agentConfigs.setInclPublicMethods(this.inclPublicMethods);
    }

    if (this.inclStaticConstructors != null) {
      agentConfigs.setInclStaticConstructors(this.inclStaticConstructors);
    }

    if (this.inclDeprecatedMethods != null) {
      agentConfigs.setInclDeprecatedMethods(this.inclDeprecatedMethods);
    }

    return agentConfigs;
  }
}
