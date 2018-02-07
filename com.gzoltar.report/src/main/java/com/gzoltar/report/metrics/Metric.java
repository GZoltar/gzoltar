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
