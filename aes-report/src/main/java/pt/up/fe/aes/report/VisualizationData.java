package pt.up.fe.aes.report;

import java.util.List;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.model.Node.Type;
import pt.up.fe.aes.base.model.Tree;
import pt.up.fe.aes.base.spectrum.Spectrum;

public class VisualizationData {

	private Spectrum spectrum;
	private List<Integer> nodeFrequency;

	public VisualizationData(Spectrum spectrum) {
		this.spectrum = spectrum;
		this.nodeFrequency = spectrum.getTestFrequencyPerNode();
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

	public String serializeSpectrum() {

		Tree t = spectrum.getTree();
		StringBuilder sb = new StringBuilder();
		sb.append("mydata = {");

		sb.append("\"tests\":[");
		for (int transaction = 0; transaction < spectrum.getTransactionsSize(); transaction++) {
			serializeTransaction(transaction, sb);
		}

		sb.append("],\"tree\":");

		Node n = t.getRoot();
		serializeNode(n, sb);

		sb.append("}");
		return sb.toString();
	}

	private void serializeNode(Node n, StringBuilder sb) {

		if (n.isLeaf()) {
			sb.append("{");
			sb.append(String.format("\"name\":\"%s\",", n.getShortName()));
			//probe id and score
			int id = spectrum.getProbeOfNode(n.getId());

			double score = 0d;
			double max = Math.max(nodeFrequency.get(n.getId()), getMaxFrequencyEstimate(n));
			double hue = nodeFrequency.get(n.getId()) / (double)spectrum.getTransactionsSize();
			hue = 2.0 * Math.abs(0.5 - hue);
			score = hue * nodeFrequency.get(n.getId()) / max;

			sb.append(String.format("\"cid\":%d,\"score\":%f", id, score));
			sb.append("}");
		}
		else {
			List<Node> children = n.getChildren();
			if (children.size() < 2 && children.get(0).getType() == Type.PACKAGE) {
				//TODO: pass the name of the node to child
				serializeNode(children.get(0), sb);
			} else {
				sb.append("{");
				sb.append(String.format("\"name\":\"%s\",", n.getShortName()));
				//children
				sb.append("\"children\":[");
				for (Node child : n.getChildren()) {
					serializeNode(child, sb);
					sb.append(",");
				}
				sb.append("]");
				sb.append("}");
			}
		}

	}

	private void serializeTransaction(int t, StringBuilder sb) {
		sb.append(spectrum.getActiveComponentsInTransaction(t).toString() + ",");
	}

	private double getMaxFrequencyEstimate(Node n) {
		Node classNode = n.getNodeOfType(Type.CLASS);

		if (classNode != null) {
			return classNode.getLeafNodes().size();
		}
		else return 1;
	}

}
