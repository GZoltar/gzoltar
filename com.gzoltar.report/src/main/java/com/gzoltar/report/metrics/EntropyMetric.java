package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;

public class EntropyMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    double entropy = 0.0;
    for (Node node : spectrum.getTargetNodes()) {
      double suspiciousness = node.getSuspiciousnessValue(this.getFormula().getName());
      if (Double.compare(suspiciousness, 0.0) > 0) {
        entropy += suspiciousness * this.log2(suspiciousness);
      }
    }

    return -1.0 * entropy;
  }

  @Override
  public String getName() {
    assert this.getFormula() != null;
    return "Entropy_" + this.getFormula().getName();
  }

  @Override
  public boolean requireFormula() {
    return true;
  }
}
