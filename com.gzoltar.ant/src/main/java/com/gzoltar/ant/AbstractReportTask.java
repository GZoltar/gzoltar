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
    this.agentConfigs.setGranularity(granularity);
  }

  /**
   * 
   * @param inclPublicMethods
   */
  public void setInclPublicMethods(Boolean inclPublicMethods) {
    this.agentConfigs.setInclPublicMethods(inclPublicMethods);
  }

  /**
   * 
   * @param inclStaticConstructors
   */
  public void setInclStaticConstructors(Boolean inclStaticConstructors) {
    this.agentConfigs.setInclStaticConstructors(inclStaticConstructors);
  }

  /**
   * 
   * @param inclDeprecatedMethods
   */
  public void setInclDeprecatedMethods(Boolean inclDeprecatedMethods) {
    this.agentConfigs.setInclDeprecatedMethods(inclDeprecatedMethods);
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
