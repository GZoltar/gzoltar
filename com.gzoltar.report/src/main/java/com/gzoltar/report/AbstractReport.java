package com.gzoltar.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.AmbiguityMetric;
import com.gzoltar.report.metrics.CoverageMetric;
import com.gzoltar.report.metrics.DDUMetric;
import com.gzoltar.report.metrics.EntropyMetric;
import com.gzoltar.report.metrics.Metric;
import com.gzoltar.report.metrics.RhoMetric;
import com.gzoltar.report.metrics.SimpsonMetric.InvertedSimpsonMetric;

public abstract class AbstractReport {

  private ISpectrum spectrum;
  private List<Metric> metrics;

  protected final String granularity;

  public AbstractReport(ISpectrum spectrum, String granularity) {
    this.spectrum = spectrum;
    this.granularity = granularity;
  }

  protected ISpectrum getSpectrum() {
    return spectrum;
  }

  protected boolean hasActiveTransactions() {
    return this.getSpectrum().getNumberOfTransactions() > 0;
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
    ISpectrum spectrum = getSpectrum();

    StringBuilder sb = new StringBuilder();
    for (Node node : spectrum.getTargetNodes()) {
      sb.append(";");
      sb.append(node.getFullName());
    }
    output.add(sb.toString());

    for (Transaction transaction : spectrum.getTransactions()) {
      sb.setLength(0);
      sb.append(transaction.getName());

      for (Node node : spectrum.getTargetNodes()) {
        if (transaction.isNodeActived(node)) {
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
