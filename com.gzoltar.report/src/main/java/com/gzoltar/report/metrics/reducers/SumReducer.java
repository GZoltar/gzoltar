package com.gzoltar.report.metrics.reducers;

import com.gzoltar.report.metrics.IMetric;

public class SumReducer extends AbstractMetricReducer {

  public SumReducer(final IMetric... metrics) {
    super(metrics);
  }

  @Override
  protected double startValue() {
    return 0d;
  }

  @Override
  protected double reduce(final double value1, final double value2) {
    return value1 + value2;
  }

}
