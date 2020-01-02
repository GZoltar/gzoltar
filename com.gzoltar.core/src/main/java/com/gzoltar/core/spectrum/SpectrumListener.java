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
package com.gzoltar.core.spectrum;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class SpectrumListener implements IEventListener {

  protected Spectrum spectrum;

  public SpectrumListener() {
    this.resetSpectrum();
  }

  public void resetSpectrum() {
    this.spectrum = new Spectrum();
  }

  public ISpectrum getSpectrum() {
    return this.spectrum;
  }

  @Override
  public void regiterProbeGroup(final ProbeGroup probeGroup) {
    this.spectrum.addProbeGroup(probeGroup);
  }

  @Override
  public void endTransaction(final Transaction transaction) {
    this.spectrum.addTransaction(transaction);
  }

  @Override
  public void endSession() {
    // empty
  }
}
