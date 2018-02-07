package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public class CoverageMetric extends AbstractMetric {

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
    return "Coverage";
  }
}
