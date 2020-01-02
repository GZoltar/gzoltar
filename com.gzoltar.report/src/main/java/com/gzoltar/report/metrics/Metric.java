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

public enum Metric {

  /** {@see com.gzoltar.report.metrics.AmbiguityMetric} */
  AMBIGUITY(new AmbiguityMetric()),

  /** {@see com.gzoltar.report.metrics.CoverageMetric} */
  COVERAGE(new CoverageMetric()),

  /** {@see com.gzoltar.report.metrics.DDUMetric} */
  DDU(new DDUMetric()),

  /** {@see com.gzoltar.report.metrics.DDUMetric.GlobalDDUMetric} */
  GLOBALDDU(new DDUMetric.GlobalDDUMetric()),

  /** {@see com.gzoltar.report.metrics.EntropyMetric} */
  ENTROPY(new EntropyMetric()),

  /** {@see com.gzoltar.report.metrics.RhoMetric} */
  RHO(new RhoMetric()),

  /** {@see com.gzoltar.report.metrics.RhoMetric.NormalizedRho} */
  NORMALIZED_RHO(new RhoMetric.NormalizedRho()),

  /** {@see com.gzoltar.report.metrics.SimpsonMetric} */
  SIMPSON(new SimpsonMetric()),

  /** {@see com.gzoltar.report.metrics.SimpsonMetric.InvertedSimpsonMetric} */
  INVERTED_SIMPSON(new SimpsonMetric.InvertedSimpsonMetric()),

  /** {@see com.gzoltar.report.metrics.SimpsonMetric.GlobalSimpsonMetric} */
  GLOBAL_SIMPSON(new SimpsonMetric.GlobalSimpsonMetric()),

  /** {@see com.gzoltar.report.metrics.SimpsonMetric.GlobalInvertedSimpsonMetric} */
  GLOBAL_INVERTED_SIMPSON(new SimpsonMetric.GlobalInvertedSimpsonMetric());

  private final IMetric metric;

  private Metric(final IMetric metric) {
    this.metric = metric;
  }

  public IMetric getMetric() {
    return this.metric;
  }
}
