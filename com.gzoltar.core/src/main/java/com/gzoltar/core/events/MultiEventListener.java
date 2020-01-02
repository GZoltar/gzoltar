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
