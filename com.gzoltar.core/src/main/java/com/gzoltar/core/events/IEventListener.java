package com.gzoltar.core.events;

import com.gzoltar.core.model.NodeType;

public interface IEventListener {

  public void endTransaction(final String transactionName, final boolean[] activity,
      final boolean isError);

  public void endTransaction(final String transactionName, final boolean[] activity,
      final int hashCode, final boolean isError);

  public void addNode(final int id, final String name, final NodeType type, final int parentId);

  public void addProbe(final int id, final int nodeId);

  public void endSession();

}
