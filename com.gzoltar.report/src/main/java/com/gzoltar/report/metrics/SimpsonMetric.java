package com.gzoltar.report.metrics;

import java.util.LinkedHashMap;
import java.util.Map;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.spectrum.ISpectrum;

public class SimpsonMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    Map<Integer, Integer> species = new LinkedHashMap<Integer, Integer>();
    for (Transaction transaction : spectrum.getTransactions()) {
      int hash = this.getHash(transaction);
      if (species.containsKey(hash)) {
        species.put(hash, species.get(hash) + 1);
      } else {
        species.put(hash, 1);
      }
    }

    double n = 0.0;
    double N = 0.0;
    for (int s : species.keySet()) {
      double ni = species.get(s);

      n += (ni * (ni - 1));
      N += ni;
    }

    double diversity = ((N == 0.0) || ((N - 1) == 0)) ? 1.0 : n / (N * (N - 1));
    return diversity;
  }

  protected int getHash(final Transaction transaction) {
    return transaction.getActivity().hashCode();
  }

  @Override
  public String getName() {
    return "Simpson";
  }

  public static class InvertedSimpsonMetric extends SimpsonMetric {
    @Override
    public double calculate(final ISpectrum spectrum) {
      return 1d - super.calculate(spectrum);
    }

    @Override
    public String getName() {
      return "Inverted Simpson";
    }
  }

  public static class GlobalInvertedSimpsonMetric extends InvertedSimpsonMetric {
    @Override
    protected int getHash(final Transaction transaction) {
      return transaction.hashCode();
    }

    @Override
    public String getName() {
      return "Global Inverted Simpson";
    }
  }

  public static class GlobalSimpsonMetric extends SimpsonMetric {
    @Override
    protected int getHash(final Transaction transaction) {
      return transaction.hashCode();
    }

    @Override
    public String getName() {
      return "Global Simpson";
    }
  }

}
