package com.gzoltar.report.metrics;

import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.spectrum.ISpectrum;

public class AmbiguityMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    Set<Integer> ambiguityGroups = new LinkedHashSet<Integer>();
    for (ProbeGroup probeGroup : spectrum.getProbeGroups()) {
      for (Probe probe : probeGroup.getProbes()) {
        BitSet bs = new BitSet();

        int t = 0;
        for (Transaction transaction : spectrum.getTransactions()) {
          if (transaction.isNodeActived(probeGroup.getHash(), probe.getArrayIndex())) {
            bs.set(t);
          }
          t++;
        }
 
        ambiguityGroups.add(bs.hashCode());
      }
    }

    int components = spectrum.getNumberOfNodes();
    int groups = ambiguityGroups.size();

    double ambiguity = (double) groups / (double) components;
    return ambiguity;
  }

  @Override
  public String getName() {
    return "Uniqueness";
  }
}
