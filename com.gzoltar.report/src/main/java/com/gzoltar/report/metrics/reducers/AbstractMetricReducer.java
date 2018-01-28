package com.gzoltar.report.metrics.reducers;

import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.metrics.Metric;

public abstract class AbstractMetricReducer implements Metric {

  private final Metric metrics[];

  public AbstractMetricReducer(final Metric... metrics) {
    this.metrics = metrics;
  }

  @Override
  public double calculate(final ISpectrum spectrum) {
    double tmp = startValue();

    for (Metric metric : this.metrics) {
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
