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

import com.gzoltar.core.model.Node;
import com.gzoltar.core.spectrum.ISpectrum;

public class EntropyMetric extends AbstractMetric {

  @Override
  public double calculate(final ISpectrum spectrum) {
    if (!validMatrix(spectrum)) {
      return 0;
    }

    double entropy = 0.0;
    for (Node node : spectrum.getNodes()) {
      double suspiciousness = node.getSuspiciousnessValue(this.getFormula().getName());
      if (Double.compare(suspiciousness, 0.0) > 0) {
        entropy += suspiciousness * this.log2(suspiciousness);
      }
    }

    return -1.0 * entropy;
  }

  @Override
  public String getName() {
    assert this.getFormula() != null;
    return "Entropy_" + this.getFormula().getName();
  }

  @Override
  public boolean requireFormula() {
    return true;
  }
}
