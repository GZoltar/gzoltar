package com.gzoltar.report.metrics;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;


public class AmbiguityMetric extends AbstractMetric {

  @Override
  public double calculate() {

    if (!validMatrix())
      return 0;

    Set<Integer> ambiguityGroups = new HashSet<Integer>();

    for (Node node : this.spectrum.getTargetNodes()) {
      BitSet bs = new BitSet();

      int t = 0;
      for (Transaction transaction : this.spectrum.getTransactions()) {
        if (transaction.isNodeActived(node)) {
          bs.set(t);
        }
        t++;
      }

      ambiguityGroups.add(bs.hashCode());
    }

    int components = this.spectrum.getNumberOfTargetNodes();
    int groups = ambiguityGroups.size();

    double ambiguity = (double) groups / (double) components;

    return ambiguity;
  }

  @Override
  public String getName() {
    return "Uniqueness";
  }

}
