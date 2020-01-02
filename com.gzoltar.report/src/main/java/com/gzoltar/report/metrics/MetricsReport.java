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
package com.gzoltar.report.metrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.AbstractReport;

public class MetricsReport extends AbstractReport {

  private final static String STATS_FILE_NAME = "statistics.csv";

  private final List<IFormula> formulas;

  public MetricsReport(final File outputDirectory, final List<IMetric> metrics,
      final List<IFormula> formulas) {
    super(outputDirectory, metrics);
    this.formulas = formulas;
  }

  @Override
  public void generateReport(ISpectrum spectrum) throws IOException {
    List<IMetric> metrics = this.getMetrics();
    if (metrics == null || metrics.isEmpty()) {
      return;
    }

    if (!this.getOutputDirectory().exists()) {
      this.getOutputDirectory().mkdirs();
    }

    File out = new File(this.getOutputDirectory() + File.separator + STATS_FILE_NAME);
    boolean toAppendData = false;
    if (out.exists()) {
      toAppendData = true;
    }

    PrintWriter metricsWriter =
        new PrintWriter(new OutputStreamWriter(new FileOutputStream(out, toAppendData), "UTF-8"));

    if (!toAppendData) {
      // header // TODO it would be nice to also have fl_family before formula
      metricsWriter.println("formula,metric_name,metric_value");
    }

    // content
    for (IFormula formula : this.formulas) {
      for (IMetric metric : metrics) {
        Double value = null;
        if (metric.requireFormula()) {
          metric.setFormula(formula);
        }
        value = metric.calculate(spectrum);
        assert value != null;
        assert !value.isNaN();

        metricsWriter.println(
            formula.getName().toLowerCase() + "," + metric.getName().toLowerCase() + "," + value);
      }
    }

    metricsWriter.close();
  }
}
