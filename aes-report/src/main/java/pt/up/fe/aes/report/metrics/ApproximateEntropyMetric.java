package pt.up.fe.aes.report.metrics;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.metrics.SimpsonMetric.InvertedSimpsonMetric;
import pt.up.fe.aes.report.metrics.reducers.MultiplicationReducer;

public class ApproximateEntropyMetric implements Metric {

	private Metric metric = new MultiplicationReducer(
									new RhoMetric(), 
									new InvertedSimpsonMetric(), 
									new AmbiguityMetric());
	
	@Override
	public void setSpectrum(Spectrum spectrum) {
		metric.setSpectrum(spectrum);
	}

	@Override
	public double calculate() {
		return metric.calculate();
	}

}
