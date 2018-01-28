package com.gzoltar.report.metrics.experimental;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.AbstractMetric;

public class DistinctTransactionsRho extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    Map<Integer, Set<Node>> distinctTransactionSet = new HashMap<Integer, Set<Node>>();

    for (Transaction transaction : spectrum.getTransactions()) {
      distinctTransactionSet.put(this.getHash(transaction), transaction.getActivity());
    }

    int components = spectrum.getNumberOfTargetNodes();
    int transactions = distinctTransactionSet.size();
    int activity_counter = 0;

    for (Set<Node> activity : distinctTransactionSet.values()) {
      activity_counter += activity.size();
    }

    double rho = (double) activity_counter / (((double) components) * ((double) transactions));
    return rho;
  }

  @Override
  public String getName() {
    return "Distinct Transactions Rho";
  }

  protected int getHash(final Transaction transaction) {
    return transaction.getActivity().hashCode();
  }

  public static class GlobalDistinctTransactionsRho extends DistinctTransactionsRho {
    @Override
    protected int getHash(final Transaction transaction) {
      return transaction.hashCode();
    }
  }
}
