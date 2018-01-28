package com.gzoltar.report.metrics;

import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public class AmbiguityMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    Set<Integer> ambiguityGroups = new LinkedHashSet<Integer>();
    for (Node node : spectrum.getTargetNodes()) {
      BitSet bs = new BitSet();

      int t = 0;
      for (Transaction transaction : spectrum.getTransactions()) {
        if (transaction.isNodeActived(node)) {
          bs.set(t);
        }
        t++;
      }

      ambiguityGroups.add(bs.hashCode());
    }

    int components = spectrum.getNumberOfTargetNodes();
    int groups = ambiguityGroups.size();

    double ambiguity = (double) groups / (double) components;
    return ambiguity;
  }

  @Override
  public String getName() {
    return "Uniqueness";
  }
}
