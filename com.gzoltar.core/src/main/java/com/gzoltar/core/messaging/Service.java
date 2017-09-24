package com.gzoltar.core.messaging;

import com.gzoltar.core.events.IEventListener;

public interface Service {

  public IEventListener getEventListener();

  public void interrupted();

  public void terminated();

  public interface ServiceFactory {
    public Service create(final String id);
  }

}
