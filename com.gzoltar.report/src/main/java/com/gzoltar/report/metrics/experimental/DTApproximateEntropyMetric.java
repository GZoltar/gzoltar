package com.gzoltar.report.metrics.experimental;

import com.gzoltar.report.metrics.AmbiguityMetric;
import com.gzoltar.report.metrics.DDUMetric;
import com.gzoltar.report.metrics.IMetric;
import com.gzoltar.report.metrics.experimental.DistinctTransactionsRho.GlobalDistinctTransactionsRho;
import com.gzoltar.report.metrics.reducers.MultiplicationReducer;

public class DTApproximateEntropyMetric extends DDUMetric {

  @Override
  protected IMetric generateMetric() {
    return new MultiplicationReducer(new DistinctTransactionsRho(), new AmbiguityMetric());
  }

  @Override
  public String getName() {
    return "DTR Approximate Entropy";
  }

  public static class GlobalDTApproximateEntropyMetric extends DTApproximateEntropyMetric {
    @Override
    protected IMetric generateMetric() {
      return new MultiplicationReducer(new GlobalDistinctTransactionsRho(), new AmbiguityMetric());
    }
  }
}
