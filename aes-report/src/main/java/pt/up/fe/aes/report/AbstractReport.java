package pt.up.fe.aes.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.up.fe.aes.base.spectrum.Spectrum;
import pt.up.fe.aes.report.metrics.AmbiguityMetric;
import pt.up.fe.aes.report.metrics.ApproximateEntropyMetric;
import pt.up.fe.aes.report.metrics.ApproximateEntropyMetric.NewApproximateEntropyMetric;
import pt.up.fe.aes.report.metrics.CoverageMetric;
import pt.up.fe.aes.report.metrics.EntropyMetric;
import pt.up.fe.aes.report.metrics.EntropyMetric.NewEntropyMetric;
import pt.up.fe.aes.report.metrics.Metric;
import pt.up.fe.aes.report.metrics.RhoMetric;
import pt.up.fe.aes.report.metrics.SimpsonMetric;
import pt.up.fe.aes.report.metrics.SimpsonMetric.NewSimpsonMetric;

public abstract class AbstractReport {

	private Spectrum spectrum;
	private List<Metric> metrics;
	
	protected final String granularity;
	
	public AbstractReport(Spectrum spectrum, String granularity) {
		this.spectrum = spectrum;
		this.granularity = granularity;
	}
	
	protected Spectrum getSpectrum() {
		return spectrum;
	}
	
	protected boolean hasActiveTransactions() {
		return getSpectrum().getTransactionsSize() > 0;
	}
	
	protected List<Metric> getMetrics() {
		if(metrics == null) {
			metrics = new ArrayList<Metric>();
			Collections.addAll(metrics, new RhoMetric(), new SimpsonMetric(),
					new AmbiguityMetric(), new ApproximateEntropyMetric(), new EntropyMetric(),
					new CoverageMetric(granularity), new NewSimpsonMetric(), new NewApproximateEntropyMetric(),
					new NewEntropyMetric());

			for(Metric metric : metrics) {
				metric.setSpectrum(getSpectrum());
			}
		}
		return metrics;
	}
	
	public List<String> getReport() {
		List<String> scores = new ArrayList<String>();
		
		addDescription(scores);
		for(Metric metric : getMetrics()) {
			scores.add(metric.getName() + ": " + String.format("%.4f", metric.calculate()));
		}

		return scores;
	}

	protected abstract void addDescription(List<String> scores);
	
}
