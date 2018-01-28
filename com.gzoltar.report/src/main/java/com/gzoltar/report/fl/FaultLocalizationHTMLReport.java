package com.gzoltar.report.fl;

import java.io.File;
import java.io.IOException;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.report.AbstractReport;

public class FaultLocalizationHTMLReport extends AbstractReport {

  public FaultLocalizationHTMLReport(final File outputDirectory) {
    super(outputDirectory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void generateReport(final ISpectrum spectrum) throws IOException {
    // TODO
  }
}
