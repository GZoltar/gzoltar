package com.gzoltar.core.events;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;

public class EmptyEventListener implements IEventListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void addNode(final Node node) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endTransaction(final Transaction transaction) {
    // NO-OP
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endSession() {
    // NO-OP
  }
}
