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

import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.spectrum.ISpectrum;

public class RhoMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    int transactions = spectrum.getNumberOfTransactions();
    int components = spectrum.getNumberOfNodes();

    int activity_counter = 0;
    for (Transaction transaction : spectrum.getTransactions()) {
      for (ProbeGroup probeGroup : spectrum.getProbeGroups()) {
        for (Probe probe : probeGroup.getProbes()) {
          if (transaction.isProbeActived(probeGroup, probe.getArrayIndex())) {
            activity_counter++;
          }
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
