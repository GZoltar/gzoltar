package pt.up.fe.ddu.report.metrics;

import pt.up.fe.ddu.base.spectrum.Spectrum;
import pt.up.fe.ddu.report.metrics.RhoMetric.NormalizedRho;
import pt.up.fe.ddu.report.metrics.SimpsonMetric.GlobalInvertedSimpsonMetric;
import pt.up.fe.ddu.report.metrics.SimpsonMetric.InvertedSimpsonMetric;
import pt.up.fe.ddu.report.metrics.reducers.MultiplicationReducer;

public class DDUMetric implements Metric {

	private Metric metric;
	
	public DDUMetric() {
		this.metric = generateMetric();
	}
	
	protected Metric generateMetric() {
		return new MultiplicationReducer(
						new NormalizedRho(), 
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
		return "DDU";
	}
	
	public static class GlobalDDUMetric extends DDUMetric {
		@Override
		protected Metric generateMetric() {
			return new MultiplicationReducer(
						new NormalizedRho(), 
						new GlobalInvertedSimpsonMetric(), 
						new AmbiguityMetric());
		}
	}
}
