package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.gzoltar.core.spectrum.Spectrum;

public class ReportGenerator {

  private static final String METRICS_FILE = "metrics.txt";
  private static final String SPECTRA_DIRECTORY = "spectra";
  private static final String SPECTRA_EXT = ".csv";

  private final OverallReport report;

  public ReportGenerator(String projectName, Spectrum spectrum, String granularity,
      List<String> classesToInstrument) {
    this.report = new OverallReport(projectName, spectrum, granularity, classesToInstrument);
  }

  public List<String> generate(File reportDirectory) {

    List<String> result = null;

    try {
      result = writeMetrics(reportDirectory);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return result;
  }


  private List<String> writeMetrics(File reportDirectory) throws IOException {
    File metricsFile = new File(reportDirectory, METRICS_FILE);

    List<String> scores = report.getReport();
    FileUtils.writeLines(metricsFile, scores, false);

    File spectraDirectory = new File(reportDirectory, SPECTRA_DIRECTORY);
    File projectSpectrum = new File(spectraDirectory, "spectrum" + SPECTRA_EXT);
    FileUtils.writeLines(projectSpectrum, report.exportSpectrum(), false);

    // per-package metrics
    writeFilteredReport(reportDirectory, spectraDirectory, "package",
        report.getPerPackageReports());

    // per-class metrics
    writeFilteredReport(reportDirectory, spectraDirectory, "class", report.getPerClassReports());

    return scores;
  }


  private static void writeFilteredReport(File reportDirectory, File spectraDirectory,
      String granularity, List<AbstractReport> reports) throws IOException {
    File granularitySpectraDirectory = new File(spectraDirectory, "per-" + granularity);
    File metricsFile = new File(reportDirectory, granularity + "-metrics.txt");
    List<String> scores = new ArrayList<String>();
    for (AbstractReport r : reports) {
      scores.addAll(r.getReport());
      scores.add("");
      File f = new File(granularitySpectraDirectory, r.getName() + SPECTRA_EXT);
      FileUtils.writeLines(f, r.exportSpectrum(), false);
    }
    FileUtils.writeLines(metricsFile, scores, false);
  }

}
