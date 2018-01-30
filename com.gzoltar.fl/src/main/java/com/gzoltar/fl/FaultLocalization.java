package com.gzoltar.fl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import com.gzoltar.core.spectrum.ISpectrum;
import com.gzoltar.core.spectrum.SpectrumReader;
import com.gzoltar.sfl.SFL;

public class FaultLocalization {

  private final IFaultLocalization<IFormula> fl;

  /**
   * 
   * @param flFamily
   * @param formulas
   */
  public FaultLocalization(final FaultLocalizationFamily flFamily, final List<IFormula> formulas) {
    switch (flFamily) {
      case SFL:
      default:
        this.fl = new SFL<IFormula>(formulas);
        break;
    }
  }

  /**
   * 
   * @param dataFile
   * @return
   * @throws IOException
   */
  public ISpectrum diagnose(final File dataFile) throws IOException {
    FileInputStream inStream = new FileInputStream(dataFile);

    SpectrumReader spectrumReader = new SpectrumReader(inStream);
    spectrumReader.read();
    ISpectrum spectrum = spectrumReader.getSpectrum();

    this.fl.diagnose(spectrum);

    return spectrum;
  }
}
