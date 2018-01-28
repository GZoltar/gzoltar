package com.gzoltar.core.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

public class MultiEventListener implements IEventListener {

  private List<IEventListener> eventListeners = new ArrayList<IEventListener>();

  public MultiEventListener(final IEventListener... els) {
    Collections.addAll(this.eventListeners, els);
  }

  public void add(final IEventListener el) {
    this.eventListeners.add(el);
  }

  @Override
  public void addNode(final Node node) {
    for (IEventListener el : this.eventListeners) {
      el.addNode(node);
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
