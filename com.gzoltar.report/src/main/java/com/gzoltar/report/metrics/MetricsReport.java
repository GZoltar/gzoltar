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
