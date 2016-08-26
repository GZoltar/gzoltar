package pt.up.fe.ddu.report.metrics.experimental;

import pt.up.fe.ddu.report.metrics.AmbiguityMetric;
import pt.up.fe.ddu.report.metrics.DDUMetric;
import pt.up.fe.ddu.report.metrics.Metric;
import pt.up.fe.ddu.report.metrics.experimental.DistinctTransactionsRho.GlobalDistinctTransactionsRho;
import pt.up.fe.ddu.report.metrics.reducers.MultiplicationReducer;

public class DTApproximateEntropyMetric extends DDUMetric {

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
