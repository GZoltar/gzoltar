package pt.up.fe.aes.report.metrics.reducers;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.metrics.Metric;

public abstract class AbstractMetricReducer implements Metric {

	private Metric metrics[];
	
	public AbstractMetricReducer(Metric... metrics) {
		this.metrics = metrics;
	}
	
	@Override
	public void setSpectrum(Spectrum spectrum) {
		for (Metric m : metrics) {
			m.setSpectrum(spectrum);
		}
	}

	@Override
	public double calculate() {
		double tmp = startValue();
		
		for (Metric m : metrics) {
			tmp = reduce(tmp, m.calculate());
		}
		
		return tmp;
	}
	
	protected abstract double startValue();

	protected abstract double reduce(double value1, double value2);

}
