package pt.up.fe.ddu.report.metrics;

import pt.up.fe.ddu.base.spectrum.Spectrum;

public interface Metric {

	public void setSpectrum(Spectrum spectrum);
	
	public double calculate();
	
	public String getName();
}
