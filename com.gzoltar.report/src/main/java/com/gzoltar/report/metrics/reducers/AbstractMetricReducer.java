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
package com.gzoltar.report.metrics.reducers;

import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.AbstractMetric;
import com.gzoltar.report.metrics.IMetric;

public abstract class AbstractMetricReducer extends AbstractMetric {

  private final IMetric metrics[];

  public AbstractMetricReducer(final IMetric... metrics) {
    this.metrics = metrics;
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    double tmp = startValue();

    for (IMetric metric : this.metrics) {
      tmp = reduce(tmp, metric.calculate(spectrum));
    }

    return tmp;
  }

  @Override
  public String getName() {
    return "Reducer";
  }

  protected abstract double startValue();

  protected abstract double reduce(final double value1, final double value2);

}
