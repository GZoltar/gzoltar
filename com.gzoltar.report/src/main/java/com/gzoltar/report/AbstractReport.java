package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import com.gzoltar.core.spectrum.ISpectrum;

public abstract class AbstractReport implements IReport {

  protected final File outputDirectory;

  protected AbstractReport(File outputDirectory) {
    this.outputDirectory = outputDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  /**
   * {@inheritDoc}
   */
  public abstract void generateReport(final ISpectrum spectrum) throws IOException;
}
