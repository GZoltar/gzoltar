package pt.up.fe.ddu.report.metrics.reducers;

import pt.up.fe.ddu.report.metrics.Metric;

public class SumReducer extends AbstractMetricReducer {

	public SumReducer(Metric... metrics) {
		super(metrics);
	}
	
	@Override
	protected double startValue() {
		return 0d;
	}

	@Override
	protected double reduce(double value1, double value2) {
		return value1 + value2;
	}

}
