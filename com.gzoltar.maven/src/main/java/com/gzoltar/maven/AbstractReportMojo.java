package com.gzoltar.maven;

import java.io.File;
import java.util.Locale;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

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
      defaultValue = "${project.build.directory}/gzoltar.exec")
  protected File dataFile;

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
}
