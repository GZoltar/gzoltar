package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

public class CoverageMetric extends AbstractMetric {

  private String granularity;

  public CoverageMetric(String granularity) {
    setGranularity(granularity);
  }

  public void setGranularity(String granularity) {
    this.granularity = granularity;
  }

  @Override
  public double calculate() {

    if (!validMatrix())
      return 0;

    int components = this.spectrum.getNumberOfTargetNodes();
    int activations = 0;

    for (Node node : this.spectrum.getTargetNodes()) {
      for (Transaction transaction : this.spectrum.getTransactions()) {
        if (transaction.isNodeActived(node)) {
          activations += 1;
          break;
        }
      }
    }

    double coverage = (double) activations / (double) components;

    return coverage;
  }

  @Override
  public String getName() {
    String name = "Coverage";
    if (this.granularity != null) {
      name += " [ " + this.granularity + " ]";
    }
    return name;
  }

}
