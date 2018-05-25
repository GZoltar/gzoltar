package com.gzoltar.ant;

import java.io.File;
import java.util.Locale;
import org.apache.tools.ant.BuildException;
import com.gzoltar.core.instr.InstrumentationLevel;

/**
 * Base class for creating a report.
 */
public abstract class AbstractReportTask extends AbstractCoverageTask {

  protected File buildLocation;

  protected File outputDirectory;

  protected File dataFile;

  protected String granularity;

  protected Boolean inclPublicMethods;

  protected Boolean inclStaticConstructors;

  protected Boolean inclDeprecatedMethods;

  protected AbstractReportTask() {
    super();

    // reports do not require any bytecode injection
    this.agentConfigs.setInstrumentationLevel(InstrumentationLevel.NONE);
  }

  /**
   * 
   * @param buildLocation
   */
  public void setBuildLocation(File buildLocation) {
    this.buildLocation = buildLocation;
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
   * @param granularity
   */
  public void setGranularity(String granularity) {
    this.agentConfigs.setGranularity(this.granularity);
  }

  /**
   * 
   * @param inclPublicMethods
   */
  public void setInclPublicMethods(Boolean inclPublicMethods) {
    this.agentConfigs.setInclPublicMethods(this.inclPublicMethods);
  }

  /**
   * 
   * @param inclStaticConstructors
   */
  public void setInclStaticConstructors(Boolean inclStaticConstructors) {
    this.agentConfigs.setInclStaticConstructors(this.inclStaticConstructors);
  }

  /**
   * 
   * @param inclDeprecatedMethods
   */
  public void setInclDeprecatedMethods(Boolean inclDeprecatedMethods) {
    this.agentConfigs.setInclDeprecatedMethods(this.inclDeprecatedMethods);
  }

  /**
   * 
   * @return
   */
  public boolean canGenerateReport() {
    if (this.buildLocation == null || !this.buildLocation.exists()) {
      log("Skipping GZoltar execution due to missing build location.");
      return false;
    }
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

    this.generateReport(Locale.getDefault());
  }

}
