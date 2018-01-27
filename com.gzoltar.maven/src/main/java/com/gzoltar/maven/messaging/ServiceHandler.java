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
