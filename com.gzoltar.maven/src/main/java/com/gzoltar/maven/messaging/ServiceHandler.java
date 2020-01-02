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
package com.gzoltar.maven.messaging;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.events.MultiEventListener;
import com.gzoltar.core.messaging.Service;
import com.gzoltar.core.spectrum.SpectrumListener;
import com.gzoltar.maven.AbstractGZoltarMojo;

public class ServiceHandler implements Service {

  private final AbstractGZoltarMojo mojo;

  private SpectrumListener spectrumBuilder;
  private IEventListener listener;

  public ServiceHandler(AbstractGZoltarMojo mojo) {
    this.mojo = mojo;
    spectrumBuilder = new SpectrumListener();
    listener = new MultiEventListener(spectrumBuilder);
  }

  @Override
  public IEventListener getEventListener() {
    return listener;
  }

  @Override
  public void interrupted() {
    // empty
  }

  @Override
  public void terminated() {
    // get Spectrum
  }

}
