package com.gzoltar.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.spectrum.Spectrum;
import com.gzoltar.report.metrics.AmbiguityMetric;
import com.gzoltar.report.metrics.CoverageMetric;
import com.gzoltar.report.metrics.DDUMetric;
import com.gzoltar.report.metrics.EntropyMetric;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.report.metrics.RhoMetric;
import com.gzoltar.report.metrics.RhoMetric.NormalizedRho;
import com.gzoltar.report.metrics.SimpsonMetric.InvertedSimpsonMetric;

public abstract class AbstractReport {

  private Spectrum spectrum;
  private List<Metric> metrics;

  protected final String granularity;

  public AbstractReport(Spectrum spectrum, String granularity) {
    this.spectrum = spectrum;
    this.granularity = granularity;
  }

  protected Spectrum getSpectrum() {
    return spectrum;
  }

  protected boolean hasActiveTransactions() {
    return getSpectrum().getTransactionsSize() > 0;
  }

  protected List<Metric> getMetrics() {
    if (metrics == null) {
      metrics = new ArrayList<Metric>();
      Collections.addAll(metrics, new RhoMetric(), new InvertedSimpsonMetric(),
          new AmbiguityMetric(), new DDUMetric(), new EntropyMetric(),
          new CoverageMetric(granularity));

      for (Metric metric : metrics) {
        metric.setSpectrum(getSpectrum());
      }
    }
    return metrics;
  }

  public List<String> getReport() {
    List<String> scores = new ArrayList<String>();

    addDescription(scores);
    for (Metric metric : getMetrics()) {
      scores.add(metric.getName() + ": " + String.format("%.4f", metric.calculate()));
    }

    return scores;
  }

  protected abstract void addDescription(List<String> scores);

  public abstract String getName();

  public List<String> exportSpectrum() {

    List<String> output = new ArrayList<String>();
    Spectrum spectrum = getSpectrum();

    int transactions = spectrum.getTransactionsSize();
    int components = spectrum.getComponentsSize();

    StringBuilder sb = new StringBuilder();
    for (int c = 0; c < components; c++) {
      sb.append(";");
      sb.append(spectrum.getNodeOfProbe(c).getFullName());
    }
    output.add(sb.toString());

    for (int t = 0; t < transactions; t++) {
      sb.setLength(0);
      sb.append(spectrum.getTransactionName(t));

      for (int c = 0; c < components; c++) {
        if (spectrum.isInvolved(t, c)) {
          sb.append(";1");
        } else {
          sb.append(";0");
        }
      }
      output.add(sb.toString());
    }

    return output;
  }
}
