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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.tuple.Pair;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.Probe;
import com.gzoltar.core.runtime.ProbeGroup;

public class Spectrum implements ISpectrum {

  /** <ProbeGroup hash, ProbeGroup> */
  private final Map<String, ProbeGroup> probeGroups;

  private final List<Transaction> transactions;

  /**
   * Constructs a new {@link com.gzoltar.core.spectrum.Spectrum}.
   */
  public Spectrum() {
    this.probeGroups = new LinkedHashMap<String, ProbeGroup>();
    this.transactions = new ArrayList<Transaction>();
  }

  // === ProbeGroups ===

  /**
   * Register a new probe group.
   */
  public void addProbeGroup(final ProbeGroup probeGroup) {
    // Mocking frameworks, application servers, or persistence frameworks may cause GZoltar to see
    // the same class several times
    if (!this.probeGroups.containsKey(probeGroup.getHash())) {
      this.probeGroups.put(probeGroup.getHash(), probeGroup);
    }
  }

  /**
   * Checks whether a probe group has been registered.
   */
  public boolean containsProbeGroup(final ProbeGroup probeGroup) {
    return this.probeGroups.containsKey(probeGroup.getHash());
  }

  /**
   * Checks whether a probe group has been registered.
   */
  public boolean containsProbeGroupByHash(final String hash) {
    return this.probeGroups.containsKey(hash);
  }

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroup(final ProbeGroup probeGroup) {
    return this.containsProbeGroup(probeGroup) ? this.probeGroups.get(probeGroup.getHash()) : null;
  }

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroupByHash(final String hash) {
    return this.containsProbeGroupByHash(hash) ? this.probeGroups.get(hash) : null;
  } 

  /**
   * Returns all {@link com.gzoltar.core.runtime.ProbeGroup} that have been registered.
   */
  public Collection<ProbeGroup> getProbeGroups() {
    return this.probeGroups.values();
  }

  // === Nodes ===

  /**
   * Returns all {@link com.gzoltar.core.model.Node} objects registered in each
   * {@link com.gzoltar.core.runtime.ProbeGroup}.
   */
  public List<Node> getNodes() {
    List<Node> nodes = new ArrayList<Node>();
    for (ProbeGroup probeGroup : this.probeGroups.values()) {
      nodes.addAll(probeGroup.getNodes());
    }
    return nodes;
  }

  /**
   * Returns the number of all {@link com.gzoltar.core.model.Node} objects registered in each
   * {@link com.gzoltar.core.runtime.ProbeGroup}.
   */
  public int getNumberOfNodes() {
    return this.getNodes().size();
  }

  /**
   * Returns all executed {@link com.gzoltar.core.model.Node} objects of a particular
   * {@link com.gzoltar.core.model.Transaction} object.
   */
  public List<Node> getHitNodes(Transaction transaction) {
    List<Node> nodes = new ArrayList<Node>();

    for (Entry<String, Pair<String, boolean[]>> activity : transaction.getActivity().entrySet()) {
      String probeGroupHash = activity.getKey();
      ProbeGroup probeGroup = this.probeGroups.get(probeGroupHash);
      boolean[] hitArray = activity.getValue().getRight();
      assert hitArray.length == probeGroup.getNumberOfProbes();

      for (Probe probe : probeGroup.getProbes()) {
        if (hitArray[probe.getArrayIndex()]) {
          nodes.add(probe.getNode());
        }
      }
    }

    return nodes;
  }

  // === Transactions ===

  /**
   * Registers a {@link com.gzoltar.core.model.Transaction}.
   */
  public void addTransaction(final Transaction transaction) {
    if (transaction.hasActivations()) {
      this.transactions.add(transaction);
    }
  }

  /**
   * Returns all {@link com.gzoltar.core.model.Transaction} that have been registered.
   */
  public List<Transaction> getTransactions() {
    return this.transactions;
  }

  /**
   * Returns the number of all {@link com.gzoltar.core.model.Transaction} that have been registered.
   */
  public int getNumberOfTransactions() {
    return this.transactions.size();
  }

  // === Overrides ===

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    List<Node> nodes = this.getNodes();
    for (Node node : nodes) {
      sb.append(node.toString());
      sb.append("\n");
    }

    for (Transaction transaction : this.transactions) {
      sb.append(transaction.toString());
      sb.append("\n");
    }

    sb.append("\nNumber of target nodes: " + this.getNumberOfNodes() + "\n");
    sb.append("Number of transactions: " + this.transactions.size() + "\n");

    return sb.toString();
  }
}
