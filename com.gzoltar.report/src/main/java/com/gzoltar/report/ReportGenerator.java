package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import com.gzoltar.core.spectrum.ISpectrum;

public class ReportGenerator {

  private static final String METRICS_FILE = "metrics.txt";
  private static final String SPECTRA_DIRECTORY = "spectra";
  private static final String SPECTRA_EXT = ".csv";

  private final OverallReport report;

  public ReportGenerator(String projectName, ISpectrum spectrum, String granularity,
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

    return scores;
  }

}
