package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.IMetric;

public abstract class AbstractReport implements IReport {

  private final File outputDirectory;

  private final List<IMetric> metrics;

  protected AbstractReport(final File outputDirectory, final List<IMetric> metrics) {
    this.outputDirectory = outputDirectory;
    this.metrics = metrics;
  }

  /**
   * {@inheritDoc}
   */
  public File getOutputDirectory() {
    return this.outputDirectory;
  }

  public List<IMetric> getMetrics() {
    return this.metrics;
  }

  /**
   * {@inheritDoc}
   */
  public abstract void generateReport(final ISpectrum spectrum) throws IOException;
}
