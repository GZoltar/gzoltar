package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.IReport;
import com.gzoltar.report.metrics.MetricsReport;

public class FaultLocalizationReport implements IReport {

  private final IReport report;

  /**
   * 
   * @param report
   */
  public FaultLocalizationReport(final IReport report) {
    this.report = report;
  }

  /**
   * {@inheritDoc}
   */
  public File getOutputDirectory() {
    return this.report.getOutputDirectory();
  }

  /**
   * {@inheritDoc}
   */
  public List<IFormula> getFormulas() {
    return this.report.getFormulas();
  }

  /**
   * {@inheritDoc}
   */
  public void generateReport(final ISpectrum spectrum) throws IOException {
    System.out.println(spectrum.toString()); // FIXME to be removed
    this.report.generateReport(spectrum);

    // apart from providing a fault localization report in here a text-based metrics reports is also
    // generated
    MetricsReport metricsReport = new MetricsReport(this.getOutputDirectory(), this.getFormulas());
    metricsReport.generateReport(spectrum);
  }
}
