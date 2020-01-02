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
