package pt.up.fe.aes.report;

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.model.Node.Type;
import pt.up.fe.aes.base.spectrum.Spectrum;

public class OverallReport extends AbstractReport {

	private final String projectName;
	private final List<String> classesToInstrument;

	public OverallReport(String projectName, Spectrum spectrum, String granularity, List<String> classesToInstrument) {
		super(spectrum, granularity);
		this.projectName = projectName;
		this.classesToInstrument = classesToInstrument;
	}

	@Override
	protected void addDescription(List<String> scores) {
		String instrumentationDescription = "";

		if (classesToInstrument != null && !classesToInstrument.isEmpty()) {
			StringBuilder sb = new StringBuilder(" [ ");

			for(int i = 0; i < classesToInstrument.size(); i++) {
				if (i != 0) {
					sb.append(" , ");
				}
				sb.append(classesToInstrument.get(i));
			}

			sb.append(" ] ");
			instrumentationDescription = sb.toString();
		}

		scores.add("Metric scores for project " + projectName  + instrumentationDescription + ":");
	}

	//create class-based reports
	public List<AbstractReport> getPerClassReports() {
		List<Node> classNodes = getSpectrum().getTree().getNodesOfType(Type.CLASS);
		List<AbstractReport> reports = new ArrayList<AbstractReport>();

		for (Node node : classNodes) {
			FilteredReport fr = new FilteredReport(getSpectrum(), granularity, node);
			if (fr.hasActiveTransactions()) {
				reports.add(fr);
			}
		}

		return reports;
	}

	public List<AbstractReport> getPerPackageReports() {
		List<Node> classNodes = getSpectrum().getTree().getNodesOfType(Type.PACKAGE);
		List<AbstractReport> reports = new ArrayList<AbstractReport>();

		for (Node node : classNodes) {
			if (node.hasChildrenOfType(Type.CLASS)) {
				FilteredReport fr = new FilteredReport(getSpectrum(), granularity, node);
				if (fr.hasActiveTransactions()) {
					reports.add(fr);
				}
			}
		}

		return reports;
	}
}
