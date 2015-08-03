package pt.up.fe.aes.report;

import java.util.List;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.model.Node.Type;
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
				double max = Math.max(nodeFrequency.get(i), getMaxFrequencyEstimate(t.getNode(i)));
				//sb.append(nodeFrequency.get(i) / max);

				double hue = nodeFrequency.get(i) / (double)spectrum.getTransactionsSize();
				hue = 2.0 * Math.abs(0.5 - hue);
				sb.append(hue * nodeFrequency.get(i) / max);
				
			}
		}
		sb.append("]");
		
		return sb.toString();
		
	}
	
	private double getMaxFrequencyEstimate(Node n) {
		Node classNode = n.getNodeOfType(Type.CLASS);
		
		if (classNode != null) {
			return classNode.getLeafNodes().size();
		}
		else return 1;
	}

}
