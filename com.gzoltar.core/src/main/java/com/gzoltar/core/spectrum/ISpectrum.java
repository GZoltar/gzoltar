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
