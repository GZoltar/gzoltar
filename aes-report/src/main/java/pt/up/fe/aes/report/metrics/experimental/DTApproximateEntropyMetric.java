package pt.up.fe.aes.report.metrics.experimental;

import pt.up.fe.aes.report.metrics.AmbiguityMetric;
import pt.up.fe.aes.report.metrics.ApproximateEntropyMetric;
import pt.up.fe.aes.report.metrics.Metric;
import pt.up.fe.aes.report.metrics.experimental.DistinctTransactionsRho.GlobalDistinctTransactionsRho;
import pt.up.fe.aes.report.metrics.reducers.MultiplicationReducer;

public class DTApproximateEntropyMetric extends ApproximateEntropyMetric {

	@Override
	protected Metric generateMetric() {
		return new MultiplicationReducer(
				new DistinctTransactionsRho(),  
				new AmbiguityMetric());
	}

	@Override
	public String getName() {
		return "DTR Approximate Entropy";
	}

	public static class GlobalDTApproximateEntropyMetric extends DTApproximateEntropyMetric {
		@Override
		protected Metric generateMetric() {
			return new MultiplicationReducer(
					new GlobalDistinctTransactionsRho(),  
					new AmbiguityMetric());
		}
	}
}
