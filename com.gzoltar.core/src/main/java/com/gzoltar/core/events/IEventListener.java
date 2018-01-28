package com.gzoltar.core.events;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

public interface IEventListener {

  public void addNode(final Node node);

  public void endTransaction(final Transaction transaction);

  public void endSession();

}
