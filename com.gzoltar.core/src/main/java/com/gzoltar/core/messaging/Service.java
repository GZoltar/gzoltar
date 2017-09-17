package com.gzoltar.core.messaging;

import com.gzoltar.core.events.IEventListener;

public interface Service {

  IEventListener getEventListener();

  void interrupted();

  void terminated();

  public interface ServiceFactory {
    Service create(String id);
  }
}
