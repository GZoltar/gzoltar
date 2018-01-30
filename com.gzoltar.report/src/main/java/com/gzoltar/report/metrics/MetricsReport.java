package com.gzoltar.report.metrics;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;
import com.gzoltar.report.AbstractReport;

public class MetricsReport extends AbstractReport {

  private final static String STATS_FILE_NAME = "statistics.csv";

  private final List<Metric> metrics;

  public MetricsReport(final File outputDirectory, final List<IFormula> formulas) {
    super(outputDirectory, formulas);

    this.metrics = new ArrayList<Metric>();
    // TODO for now list of metrics is fixed to these metrics, ideally it should be a parameter
    Collections.addAll(this.metrics, new RhoMetric(), new AmbiguityMetric());
    for (IFormula formula : this.formulas) {
      this.metrics.add(new EntropyMetric(formula));
    }
  }

  @Override
  public void generateReport(ISpectrum spectrum) throws IOException {
    if (!this.outputDirectory.exists()) {
      this.outputDirectory.mkdirs();
    }

    PrintWriter metricsWriter =
        new PrintWriter(this.outputDirectory + File.separator + STATS_FILE_NAME, "UTF-8");

    // header
    List<String> header = new ArrayList<String>();
    for (Metric metric : this.metrics) {
      header.add(metric.getName());
    }
    metricsWriter.println(StringUtils.join(header, ','));

    // content
    List<Double> values = new ArrayList<Double>();
    for (Metric metric : this.metrics) {
      Double value = metric.calculate(spectrum);
      assert value != null;
      assert !value.isNaN();
      values.add(value);
    }
    metricsWriter.println(StringUtils.join(values, ','));

    metricsWriter.close();
  }
}
