package pt.up.fe.aes.report;

import java.util.List;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.spectrum.FilteredSpectrumBuilder;
import pt.up.fe.aes.base.spectrum.Spectrum;

public class FilteredReport extends AbstractReport {

	private final FilteredSpectrumBuilder fsb = new FilteredSpectrumBuilder();
	private final Node node;
	private Spectrum generatedSpectrum;
	
	public FilteredReport(Spectrum spectrum, String granularity, Node node) {
		super(spectrum, granularity);
		this.node = node;
		
		fsb.setSource(spectrum).includeNode(node);
	}
	
	@Override 
	protected Spectrum getSpectrum() {
		if (generatedSpectrum == null) {
			generatedSpectrum = fsb.getSpectrum();
		}
		return generatedSpectrum;
	}

	@Override
	protected void addDescription(List<String> scores) {
		scores.add("Metric scores for node " + node.getFullName() + ":");
	}

}
