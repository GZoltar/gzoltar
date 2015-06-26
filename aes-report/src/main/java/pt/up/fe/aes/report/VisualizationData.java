package pt.up.fe.aes.report;

import java.util.List;

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
		
		List<Integer> nodeFrequency = spectrum.getTestFrequencyPerNode();
		
		StringBuilder sb = new StringBuilder("\"type\":\"visualization\",");
		sb.append(t.toString());
		sb.append(",");
		sb.append("\"scores\":[");
		for(int i = 0; i < size; i++) {
			if (i != 0) {
				sb.append(",");
			}
			
			if(t.getNode(i).isLeaf()) {
				sb.append(nodeFrequency.get(i) / 10d);
			}
		}
		sb.append("]");
		
		return sb.toString();
		
	}
	
}
