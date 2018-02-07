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
