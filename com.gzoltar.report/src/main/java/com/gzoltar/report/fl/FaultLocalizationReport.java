package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.fl.formatter.IFaultLocalizationReportFormatter;
import com.gzoltar.report.metrics.IMetric;
import com.gzoltar.report.metrics.MetricsReport;

public class FaultLocalizationReport extends AbstractFaultLocalizationReport {

  private final IFaultLocalizationReportFormatter reportFormatter;

  /**
   * 
   * @param outputDirectory
   * @param metrics
   * @param formulas
   * @param reportFormatter
   */
  public FaultLocalizationReport(final File outputDirectory, final List<IMetric> metrics,
      final List<IFormula> formulas, final IFaultLocalizationReportFormatter reportFormatter) {
    super(outputDirectory, metrics, formulas);
    this.reportFormatter = reportFormatter;
  }

  /**
   * {@inheritDoc}
   */
  public void generateReport(final ISpectrum spectrum) throws IOException {
    // generate txt/html report
    this.reportFormatter.generateFaultLocalizationReport(this.getOutputDirectory(), spectrum,
        this.getFormulas());

    // apart from providing a fault localization report in here a text-based metrics report is also
    // generated
    MetricsReport metricsReport =
        new MetricsReport(this.getOutputDirectory(), this.getMetrics(), this.getFormulas());
    metricsReport.generateReport(spectrum);
  }
}
