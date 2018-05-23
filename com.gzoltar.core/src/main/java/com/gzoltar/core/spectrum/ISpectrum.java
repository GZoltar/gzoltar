package com.gzoltar.core.spectrum;

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
  public boolean containsProbeGroup(final String probeGroupName);

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroup(final String probeGroupName);

  /**
   * Returns all {@link com.gzoltar.core.runtime.ProbeGroup} that have been registered.
   */
  public List<ProbeGroup> getProbeGroups();

  // === Runtime hitArray ===

  /**
   * Sets a new boolean array for a specific probeGroup.
   * 
   * Note: the probeGroup must exist.
   */
  public boolean[] getHitArray(final String probeGroupName);

  /**
   * Resets the boolean array of a specific probeGroup.
   * 
   * Note: the probeGroup must exist.
   */
  public void resetHitArray(final String probeGroupName);

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
