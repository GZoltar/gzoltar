package com.gzoltar.report.metrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
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

    PrintWriter metricsWriter =
        new PrintWriter(this.getOutputDirectory() + File.separator + STATS_FILE_NAME, "UTF-8");

    // header
    List<String> header = new ArrayList<String>();
    for (IMetric metric : metrics) {
      if (metric.requireFormula()) {
        for (IFormula formula : this.formulas) {
          metric.setFormula(formula);
          header.add(metric.getName());
        }
      } else {
        header.add(metric.getName());
      }
    }
    metricsWriter.println(StringUtils.join(header, ','));

    // content
    List<Double> values = new ArrayList<Double>();
    for (IMetric metric : metrics) {
      if (metric.requireFormula()) {
        for (IFormula formula : this.formulas) {
          metric.setFormula(formula);
          Double value = metric.calculate(spectrum);
          assert value != null;
          assert !value.isNaN();
          values.add(value);
        }
      } else {
        Double value = metric.calculate(spectrum);
        assert value != null;
        assert !value.isNaN();
        values.add(value);
      }
    }
    metricsWriter.println(StringUtils.join(values, ','));

    metricsWriter.close();
  }
}
