package com.gzoltar.maven;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.reporting.MavenReportException;
import com.gzoltar.report.FLReport;

@Mojo(name = "report", defaultPhase = LifecyclePhase.VERIFY, threadSafe = true)
public class ReportMojo extends AbstractReportMojo {

  /**
   * Output directory for the reports.
   */
  @Parameter(property = "gzoltar.outputDirectory",
      defaultValue = "${project.reporting.outputDirectory}/gzoltar")
  private File outputDirectory;

  /**
   * File with execution data.
   */
  @Parameter(property = "gzoltar.dataFile",
      defaultValue = "${project.build.directory}/gzoltar.exec")
  private File dataFile;

  @Override
  protected boolean canGenerateReportRegardingDataFiles() {
    return this.dataFile.exists();
  }

  @Override
  protected void executeReport(final Locale locale) throws MavenReportException {
    // TODO
    getLog().info("--- REPORT MOJO ---");
    
    try {
      new FLReport(this.dataFile);
    } catch (IOException e) {
      getLog().info(e);
      e.printStackTrace();
    }
  }

  @Override
  protected String getOutputDirectory() {
    return this.outputDirectory.getAbsolutePath();
  }
}
