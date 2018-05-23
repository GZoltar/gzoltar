package com.gzoltar.core.events;

import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class EmptyEventListener implements IEventListener {

  /**
   * {@inheritDoc}
   */
  @Override
  public void regiterProbeGroup(final ProbeGroup probeGroup) {
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
