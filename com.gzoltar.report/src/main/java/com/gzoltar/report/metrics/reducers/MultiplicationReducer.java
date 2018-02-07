package com.gzoltar.report.metrics.reducers;

import com.gzoltar.report.metrics.IMetric;

public class MultiplicationReducer extends AbstractMetricReducer {

  public MultiplicationReducer(final IMetric... metrics) {
    super(metrics);
  }

  @Override
  protected double startValue() {
    return 1d;
  }

  @Override
  protected double reduce(final double value1, final double value2) {
    return value1 * value2;
  }

}
