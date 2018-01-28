package com.gzoltar.report.metrics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.gzoltar.core.model.Transaction;

public class EntropyMetric extends AbstractMetric {

  protected Map<Integer, Integer> globalCounter = new LinkedHashMap<Integer, Integer>();
  protected Map<Integer, Integer> localCounter = new LinkedHashMap<Integer, Integer>();

  @Override
  public double calculate() {

    if (!validMatrix())
      return 0;

    this.globalCounter.clear();
    this.localCounter.clear();

    for (Transaction transaction : this.spectrum.getTransactions()) {
      fillCounter(transaction.hashCode(), this.globalCounter);
      fillCounter(transaction.getActivity().hashCode(), this.localCounter);
    }

    double transactions = this.spectrum.getNumberOfTransactions();
    double components = this.getComponentsSize();

    double entropy = 0.0;
    for (Entry<Integer, Integer> entry : getCounter().entrySet()) {
      double prob_i = (double) entry.getValue() / transactions;
      entropy += prob_i * log2(prob_i);
    }

    entropy = Math.abs(entropy / components);

    return entropy;
  }

  private static void fillCounter(int hash, Map<Integer, Integer> counter) {
    if (counter.containsKey(hash)) {
      counter.put(hash, counter.get(hash) + 1);
    } else {
      counter.put(hash, 1);
    }
  }

  private static double log2(double value) {
    return Math.log(value) / Math.log(2);
  }

  protected int getComponentsSize() {
    return this.spectrum.getNumberOfTargetNodes();
  }

  protected Map<Integer, Integer> getCounter() {
    return this.localCounter;
  }

  @Override
  public String getName() {
    return "Entropy";
  }

  public static class GlobalEntropyMetric extends EntropyMetric {
    @Override
    protected int getComponentsSize() {
      int delta = this.globalCounter.size() - this.localCounter.size();
      return this.spectrum.getNumberOfTargetNodes() + delta;
    }

    @Override
    protected Map<Integer, Integer> getCounter() {
      return this.globalCounter;
    }
  }
}
