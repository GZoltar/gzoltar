package com.gzoltar.fl;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import com.gzoltar.core.AgentConfigs;
import com.gzoltar.core.spectrum.FilteredSpectrum;
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
   * @param buildLocation
   * @param agentConfigs
   * @param dataFile
   * @return
   * @throws Exception
   */
  public ISpectrum diagnose(final String buildLocation, final AgentConfigs agentConfigs,
      final File dataFile) throws Exception {
    FileInputStream inStream = new FileInputStream(dataFile);

    SpectrumReader spectrumReader = new SpectrumReader(buildLocation, agentConfigs, inStream);
    spectrumReader.read();
    ISpectrum spectrum = spectrumReader.getSpectrum();

    // filter a spectrum object according to user's preferences
    FilteredSpectrum filter = new FilteredSpectrum(agentConfigs);
    ISpectrum filteredSpectrum = filter.filter(spectrum);

    this.fl.diagnose(filteredSpectrum);

    return filteredSpectrum;
  }
}
