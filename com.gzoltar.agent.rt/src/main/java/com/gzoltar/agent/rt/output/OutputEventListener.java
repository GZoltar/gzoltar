/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.agent.rt.output;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class OutputEventListener implements IEventListener {

  private final IAgentOutput output;

  public OutputEventListener(final IAgentOutput output) {
    this.output = output;
  }

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
  public synchronized void endTransaction(final Transaction transaction) {
    try {
      this.output.writeTransaction(transaction);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void endSession() {
    // NO-OP
  }
}
