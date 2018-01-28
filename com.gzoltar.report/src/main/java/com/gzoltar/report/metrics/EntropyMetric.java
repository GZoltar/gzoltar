package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;

public class EntropyMetric extends AbstractMetric {

  private final String formulaName;

  public EntropyMetric(final String formulaName) {
    this.formulaName = formulaName;
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    double entropy = 0.0;
    for (Node node : spectrum.getTargetNodes()) {
      double suspiciousness = node.getSuspiciousnessValue(this.formulaName);
      entropy += suspiciousness * this.log2(suspiciousness);
    }

    return entropy;
  }

  @Override
  public String getName() {
    return "Entropy_" + this.formulaName;
  }
}
