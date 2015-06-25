package pt.up.fe.aes.report;

import pt.up.fe.aes.base.model.Tree;
import pt.up.fe.aes.base.spectrum.Spectrum;

public class VisualizationData {

	private Spectrum spectrum;
	
	public VisualizationData(Spectrum spectrum) {
		this.spectrum = spectrum;
	}
	
	public String serialize() {
		
		Tree t = spectrum.getTree();
		int size = t.size();
		
		StringBuilder sb = new StringBuilder("\"type\":\"visualization\",");
		sb.append(t.toString());
		sb.append(",");
		sb.append("\"scores\":[");
		for(int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append("0");
		}
		sb.append("]");
		
		return sb.toString();
		
	}
	
}
