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

import java.util.BitSet;
import java.util.LinkedHashSet;
import java.util.Set;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;
import com.gzoltar.core.spectrum.ISpectrum;

public class AmbiguityMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!this.validMatrix(spectrum)) {
      return 0;
    }

    Set<Integer> ambiguityGroups = new LinkedHashSet<Integer>();
    for (ProbeGroup probeGroup : spectrum.getProbeGroups()) {
      for (Probe probe : probeGroup.getProbes()) {
        BitSet bs = new BitSet();

        int t = 0;
        for (Transaction transaction : spectrum.getTransactions()) {
          if (transaction.isProbeActived(probeGroup, probe.getArrayIndex())) {
            bs.set(t);
          }
          t++;
        }
 
        ambiguityGroups.add(bs.hashCode());
      }
    }

    int components = spectrum.getNumberOfNodes();
    int groups = ambiguityGroups.size();

    double ambiguity = (double) groups / (double) components;
    return ambiguity;
  }

  @Override
  public String getName() {
    return "Uniqueness";
  }
}
