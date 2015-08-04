package pt.up.fe.aes.report.metrics;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.metrics.SimpsonMetric.GlobalInvertedSimpsonMetric;
import pt.up.fe.aes.report.metrics.SimpsonMetric.InvertedSimpsonMetric;
import pt.up.fe.aes.report.metrics.reducers.MultiplicationReducer;

public class ApproximateEntropyMetric implements Metric {

	private Metric metric;
	
	public ApproximateEntropyMetric() {
		this.metric = generateMetric();
	}
	
	protected Metric generateMetric() {
		return new MultiplicationReducer(
						new RhoMetric(), 
						new InvertedSimpsonMetric(), 
						new AmbiguityMetric());
	}
	
	@Override
	public void setSpectrum(Spectrum spectrum) {
		metric.setSpectrum(spectrum);
	}

	@Override
	public double calculate() {
		return metric.calculate();
	}

	@Override
	public String getName() {
		return "Approximate Entropy";
	}
	
	public static class GlobalApproximateEntropyMetric extends ApproximateEntropyMetric {
		@Override
		protected Metric generateMetric() {
			return new MultiplicationReducer(
						new RhoMetric(), 
						new GlobalInvertedSimpsonMetric(), 
						new AmbiguityMetric());
		}
	}
}
