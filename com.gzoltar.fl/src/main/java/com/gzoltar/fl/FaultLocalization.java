/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
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
