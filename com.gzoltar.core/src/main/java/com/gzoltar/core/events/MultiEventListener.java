package com.gzoltar.core.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.model.NodeType;

public class MultiEventListener implements IEventListener {

  private List<IEventListener> eventListeners = new ArrayList<IEventListener>();

  public MultiEventListener(final IEventListener... els) {
    Collections.addAll(this.eventListeners, els);
  }

  public void add(final IEventListener el) {
    this.eventListeners.add(el);
  }

  @Override
  public void endTransaction(final String transactionName, final boolean[] activity,
      final boolean isError) {
    for (IEventListener el : this.eventListeners) {
      el.endTransaction(transactionName, activity, isError);
    }
  }

  @Override
  public void endTransaction(final String transactionName, final boolean[] activity,
      final int hashCode, final boolean isError) {
    for (IEventListener el : this.eventListeners) {
      el.endTransaction(transactionName, activity, hashCode, isError);
    }
  }

  @Override
  public void addNode(final int id, final String name, final NodeType type, final int parentId) {
    for (IEventListener el : this.eventListeners) {
      el.addNode(id, name, type, parentId);
    }
  }

  @Override
  public void addProbe(final int id, final int nodeId) {
    for (IEventListener el : this.eventListeners) {
      el.addProbe(id, nodeId);
    }
  }

  @Override
  public void endSession() {
    for (IEventListener el : this.eventListeners) {
      el.endSession();
    }
  }

}
