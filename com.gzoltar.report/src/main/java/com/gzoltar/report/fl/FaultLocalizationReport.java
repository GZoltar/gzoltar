package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.IReport;
import com.gzoltar.report.ReportFormat;
import com.gzoltar.report.metrics.MetricsReport;

public class FaultLocalizationReport implements IReport {

  private final IReport report;

  /**
   * 
   * @param reportFormat
   * @param flFamily
   * @param formulas
   */
  public FaultLocalizationReport(final ReportFormat reportFormat, final File outputDirectory) {
    // which format of report?
    switch (reportFormat) {
      case TXT:
      default:
        this.report = new FaultLocalizationTxtReport(outputDirectory);
        break;
      case HTML:
        this.report = new FaultLocalizationHTMLReport(outputDirectory);
        break;
    }
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
  public void generateReport(final ISpectrum spectrum) throws IOException {
    System.out.println(spectrum.toString()); // FIXME to be removed
    this.report.generateReport(spectrum);

    // apart from providing a fault localization report in here a text-based metrics reports is also
    // generated
    MetricsReport metricsReport = new MetricsReport(this.getOutputDirectory());
    metricsReport.generateReport(spectrum);
  }
}
