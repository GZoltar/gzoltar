package pt.up.fe.ddu.report.metrics;

import pt.up.fe.ddu.base.spectrum.Spectrum;

public abstract class AbstractMetric implements Metric {

	protected Spectrum spectrum;
	
	@Override
	public void setSpectrum(Spectrum spectrum) {
		this.spectrum = spectrum;
	}

	protected boolean validMatrix() {
		if (spectrum != null) {
			return spectrum.getComponentsSize() > 0 && spectrum.getTransactionsSize() > 0;
		}
		return false;
	}
	
}
