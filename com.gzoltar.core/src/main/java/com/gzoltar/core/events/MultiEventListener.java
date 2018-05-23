package com.gzoltar.core.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class MultiEventListener implements IEventListener {

  private List<IEventListener> eventListeners = new ArrayList<IEventListener>();

  public MultiEventListener(final IEventListener... els) {
    Collections.addAll(this.eventListeners, els);
  }

  public void add(final IEventListener el) {
    this.eventListeners.add(el);
  }

  @Override
  public void regiterProbeGroup(final ProbeGroup probeGroup) {
    for (IEventListener el : this.eventListeners) {
      el.regiterProbeGroup(probeGroup);
    }
  }

  @Override
  public void endTransaction(final Transaction transaction) {
    for (IEventListener el : this.eventListeners) {
      el.endTransaction(transaction);
    }
  }

  @Override
  public void endSession() {
    for (IEventListener el : this.eventListeners) {
      el.endSession();
    }
  }
}
