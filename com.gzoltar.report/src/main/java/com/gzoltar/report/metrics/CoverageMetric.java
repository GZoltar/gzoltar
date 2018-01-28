package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public class CoverageMetric extends AbstractMetric {

  private final String granularity;

  public CoverageMetric(final String granularity) {
    this.granularity = granularity;
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    int components = spectrum.getNumberOfTargetNodes();
    int activations = 0;

    for (Node node : spectrum.getTargetNodes()) {
      for (Transaction transaction : spectrum.getTransactions()) {
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
