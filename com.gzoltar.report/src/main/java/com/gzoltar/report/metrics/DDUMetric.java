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

import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.RhoMetric.NormalizedRho;
import com.gzoltar.report.metrics.SimpsonMetric.GlobalInvertedSimpsonMetric;
import com.gzoltar.report.metrics.SimpsonMetric.InvertedSimpsonMetric;
import com.gzoltar.report.metrics.reducers.MultiplicationReducer;

public class DDUMetric extends AbstractMetric {

  private final IMetric metric;

  public DDUMetric() {
    this.metric = generateMetric();
  }

  protected IMetric generateMetric() {
    return new MultiplicationReducer(new NormalizedRho(), new InvertedSimpsonMetric(),
        new AmbiguityMetric());
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    return metric.calculate(spectrum);
  }

  @Override
  public String getName() {
    return "DDU";
  }

  public static class GlobalDDUMetric extends DDUMetric {
    @Override
    protected IMetric generateMetric() {
      return new MultiplicationReducer(new NormalizedRho(), new GlobalInvertedSimpsonMetric(),
          new AmbiguityMetric());
    }
  }
}
