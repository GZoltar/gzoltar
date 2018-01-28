package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

public class RhoMetric extends AbstractMetric {

  @Override
  public double calculate() {

    if (!validMatrix())
      return 0;

    int transactions = this.spectrum.getNumberOfTransactions();
    int components = this.spectrum.getNumberOfTargetNodes();

    int activity_counter = 0;
    for (Transaction transaction : this.spectrum.getTransactions()) {
      for (Node node : this.spectrum.getTargetNodes()) {
        if (transaction.isNodeActived(node)) {
          activity_counter++;
        }
      }
    }
    double rho = (double) activity_counter / (((double) components) * ((double) transactions));

    return rho;
  }

  @Override
  public String getName() {
    return "Density";
  }


  public static class NormalizedRho extends RhoMetric {
    @Override
    public double calculate() {
      double rho = super.calculate();

      return 1.0 - Math.abs(1.0 - 2.0 * rho);
    }

    @Override
    public String getName() {
      return "Normalized Rho";
    }
  }
}
