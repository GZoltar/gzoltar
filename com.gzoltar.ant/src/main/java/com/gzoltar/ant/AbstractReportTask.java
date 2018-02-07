package com.gzoltar.ant;

import java.io.File;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Base class for creating a report.
 */
public abstract class AbstractReportTask extends Task {

  protected File outputDirectory;

  protected File dataFile;

  protected AbstractReportTask() {
    super();
  }

  /**
   * 
   * @param outputDirectory
   */
  public void setOutputDirectory(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * 
   * @param dataFile
   */
  public void setDataFile(File dataFile) {
    this.dataFile = dataFile;
  }

  /**
   * 
   * @return
   */
  public boolean canGenerateReport() {
    if (this.dataFile == null || !this.dataFile.exists()) {
      log("Skipping GZoltar execution due to missing execution data file.");
      return false;
    }
    if (this.outputDirectory == null) {
      log("Skipping GZoltar execution due to missing output directory.");
      return false;
    }
    return true;
  }

  /**
   * 
   * @param locale
   */
  protected abstract void generateReport(Locale locale);

  /**
   * {@inheritDoc}
   */
  @Override
  public void execute() throws BuildException {
    if (!this.canGenerateReport()) {
      return;
    }

    try {
      this.generateReport(Locale.getDefault());
    } catch (final BuildException e) {
      throw new BuildException("An error has occurred in GZoltar report generation.", e);
    }
  }
}
