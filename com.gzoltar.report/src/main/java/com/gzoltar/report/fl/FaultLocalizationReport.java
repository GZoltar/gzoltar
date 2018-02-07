package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.fl.format.IFaultLocalizationReportFormat;
import com.gzoltar.report.metrics.IMetric;
import com.gzoltar.report.metrics.MetricsReport;

public class FaultLocalizationReport extends AbstractFaultLocalizationReport {

  private final IFaultLocalizationReportFormat reportFormat;

  /**
   * 
   * @param outputDirectory
   * @param metrics
   * @param formulas
   * @param reportFormat
   */
  public FaultLocalizationReport(final File outputDirectory, final List<IMetric> metrics,
      final List<IFormula> formulas, final IFaultLocalizationReportFormat reportFormat) {
    super(outputDirectory, metrics, formulas);
    this.reportFormat = reportFormat;
  }

  /**
   * {@inheritDoc}
   */
  public void generateReport(final ISpectrum spectrum) throws IOException {
    // generate txt/html report
    this.reportFormat.generateFaultLocalizationReport(this.getOutputDirectory(), spectrum,
        this.getFormulas());

    // apart from providing a fault localization report in here a text-based metrics report is also
    // generated
    MetricsReport metricsReport =
        new MetricsReport(this.getOutputDirectory(), this.getMetrics(), this.getFormulas());
    metricsReport.generateReport(spectrum);
  }
}
