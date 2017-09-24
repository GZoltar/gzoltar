package com.gzoltar.report;

import java.util.ArrayList;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.spectrum.ISpectrum;

public class OverallReport extends AbstractReport {

  private final String projectName;
  private final List<String> classesToInstrument;

  public OverallReport(String projectName, ISpectrum spectrum, String granularity,
      List<String> classesToInstrument) {
    super(spectrum, granularity);
    this.projectName = projectName;
    this.classesToInstrument = classesToInstrument;
  }

  @Override
  protected void addDescription(List<String> scores) {
    String instrumentationDescription = "";

    if (classesToInstrument != null && !classesToInstrument.isEmpty()) {
      StringBuilder sb = new StringBuilder(" [ ");

      for (int i = 0; i < classesToInstrument.size(); i++) {
        if (i != 0) {
          sb.append(" , ");
        }
        sb.append(classesToInstrument.get(i));
      }

      sb.append(" ] ");
      instrumentationDescription = sb.toString();
    }

    scores.add("Metric scores for project " + projectName + instrumentationDescription + ":");
  }

  // create class-based reports
  public List<AbstractReport> getPerClassReports() {
    List<Node> classNodes = getSpectrum().getTree().getNodesOfType(NodeType.CLASS);
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
    List<Node> packageNodes = getSpectrum().getTree().getNodesOfType(NodeType.PACKAGE);
    List<AbstractReport> reports = new ArrayList<AbstractReport>();

    for (Node node : packageNodes) {
      if (node.hasChildrenOfType(NodeType.CLASS)) {
        FilteredReport fr = new FilteredReport(getSpectrum(), granularity, node);
        if (fr.hasActiveTransactions()) {
          reports.add(fr);
        }
      }
    }

    return reports;
  }

  @Override
  public String getName() {
    return projectName;
  }
}
