package com.gzoltar.core.events;

import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public interface IEventListener {

  public void regiterProbeGroup(final ProbeGroup probeGroup);

  public void endTransaction(final Transaction transaction);

  public void endSession();

}
