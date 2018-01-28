package com.gzoltar.report;

import java.util.List;
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

  @Override
  public String getName() {
    return projectName;
  }
}
