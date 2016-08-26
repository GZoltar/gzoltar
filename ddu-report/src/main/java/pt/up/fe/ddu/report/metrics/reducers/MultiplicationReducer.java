package pt.up.fe.ddu.report.metrics.reducers;

import pt.up.fe.ddu.report.metrics.Metric;

public class MultiplicationReducer extends AbstractMetricReducer {

	public MultiplicationReducer(Metric... metrics) {
		super(metrics);
	}
	
	@Override
	protected double startValue() {
		return 1d;
	}

	@Override
	protected double reduce(double value1, double value2) {
		return value1 * value2;
	}

}
