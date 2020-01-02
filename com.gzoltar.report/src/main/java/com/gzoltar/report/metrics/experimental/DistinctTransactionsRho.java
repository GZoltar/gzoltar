/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.report.metrics.experimental;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    Map<Integer, List<Node>> distinctTransactionSet = new HashMap<Integer, List<Node>>();

    for (Transaction transaction : spectrum.getTransactions()) {
      distinctTransactionSet.put(this.getHash(transaction), spectrum.getHitNodes(transaction));
    }

    int components = spectrum.getNumberOfNodes();
    int transactions = distinctTransactionSet.size();
    int activity_counter = 0;

    for (List<Node> activity : distinctTransactionSet.values()) {
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
