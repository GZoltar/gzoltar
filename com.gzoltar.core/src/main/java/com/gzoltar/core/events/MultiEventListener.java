package com.gzoltar.core.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.gzoltar.core.model.NodeType;

public class MultiEventListener implements IEventListener {

  private List<IEventListener> eventListeners = new ArrayList<IEventListener>();

  public MultiEventListener(IEventListener... els) {
    Collections.addAll(eventListeners, els);
  }

  public void add(IEventListener el) {
    eventListeners.add(el);
  }

  @Override
  public void endTransaction(String transactionName, boolean[] activity, boolean isError) {
    for (IEventListener el : eventListeners) {
      el.endTransaction(transactionName, activity, isError);
    }
  }

  @Override
  public void endTransaction(String transactionName, boolean[] activity, int hashCode,
      boolean isError) {
    for (IEventListener el : eventListeners) {
      el.endTransaction(transactionName, activity, hashCode, isError);
    }
  }

  @Override
  public void addNode(int id, String name, NodeType type, int parentId) {
    for (IEventListener el : eventListeners) {
      el.addNode(id, name, type, parentId);
    }
  }

  @Override
  public void addProbe(int id, int nodeId) {
    for (IEventListener el : eventListeners) {
      el.addProbe(id, nodeId);
    }

  }

  @Override
  public void endSession() {
    for (IEventListener el : eventListeners) {
      el.endSession();
    }
  }

}
