package com.gzoltar.report.metrics;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public class RhoMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    int transactions = spectrum.getNumberOfTransactions();
    int components = spectrum.getNumberOfTargetNodes();

    int activity_counter = 0;
    for (Transaction transaction : spectrum.getTransactions()) {
      for (Node node : spectrum.getTargetNodes()) {
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
    public double calculate(final ISpectrum spectrum) {
      double rho = super.calculate(spectrum);
      return 1.0 - Math.abs(1.0 - 2.0 * rho);
    }

    @Override
    public String getName() {
      return "Normalized Rho";
    }
  }
}
