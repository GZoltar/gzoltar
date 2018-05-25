package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.spectrum.ISpectrum;

public class CoverageMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    int components = spectrum.getNumberOfNodes();
    int activations = 0;

    for (ProbeGroup probeGroup : spectrum.getProbeGroups()) {
      probeBreak: for (Probe probe : probeGroup.getProbes()) {
        for (Transaction transaction : spectrum.getTransactions()) {
          if (transaction.isProbeActived(probeGroup, probe.getArrayIndex())) {
            activations += 1;
            break probeBreak;
          }
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
