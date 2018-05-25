package com.gzoltar.core.spectrum;

import java.util.Collection;
import java.util.List;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public interface ISpectrum {

  // === ProbeGroups ===

  /**
   * Register a new probe group.
   */
  public void addProbeGroup(final ProbeGroup probeGroup);

  /**
   * Checks whether a probe group has been registered.
   */
  public boolean containsProbeGroup(final ProbeGroup probeGroup);

  /**
   * Checks whether a probe group has been registered.
   */
  public boolean containsProbeGroupByHash(final String hash);

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroup(final ProbeGroup probeGroup);

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroupByHash(final String hash);

  /**
   * Returns all {@link com.gzoltar.core.runtime.ProbeGroup} that have been registered.
   */
  public Collection<ProbeGroup> getProbeGroups();

  // === Nodes ===

  /**
   * Returns all {@link com.gzoltar.core.model.Node} objects registered in each
   * {@link com.gzoltar.core.runtime.ProbeGroup}.
   */
  public List<Node> getNodes();

  /**
   * Returns the number of all {@link com.gzoltar.core.model.Node} objects registered in each
   * {@link com.gzoltar.core.runtime.ProbeGroup}.
   */
  public int getNumberOfNodes();

  /**
   * Returns all executed {@link com.gzoltar.core.model.Node} objects of a particular
   * {@link com.gzoltar.core.model.Transaction} object.
   */
  public List<Node> getHitNodes(Transaction transaction);

  // === Transaction ===

  /**
   * Registers a {@link com.gzoltar.core.model.Transaction}.
   */
  public void addTransaction(final Transaction transaction);

  /**
   * Returns all {@link com.gzoltar.core.model.Transaction} that have been registered.
   */
  public List<Transaction> getTransactions();

  /**
   * Returns the number of all {@link com.gzoltar.core.model.Transaction} that have been registered.
   */
  public int getNumberOfTransactions();
}
