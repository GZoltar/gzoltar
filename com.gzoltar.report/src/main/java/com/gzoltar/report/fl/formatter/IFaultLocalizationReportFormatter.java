package com.gzoltar.report.fl.formatter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.fl.IFormula;

public interface IFaultLocalizationReportFormatter {

  /**
   * 
   * @param outputDirectory
   * @param spectrum
   * @param formulas
   * @throws IOException
   */
  public void generateFaultLocalizationReport(final File outputDirectory, final ISpectrum spectrum,
      final List<IFormula> formulas) throws IOException;
}
