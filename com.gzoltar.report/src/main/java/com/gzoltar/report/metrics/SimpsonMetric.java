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
