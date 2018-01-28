package com.gzoltar.report;

import java.io.File;
import java.io.IOException;
import com.gzoltar.core.spectrum.ISpectrum;

public interface IReport {

  /**
   * 
   * @return
   */
  public File getOutputDirectory();

  /**
   * 
   * @param spectrum
   */
  public void generateReport(final ISpectrum spectrum) throws IOException;
}
