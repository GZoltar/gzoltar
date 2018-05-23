package com.gzoltar.core.spectrum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.Transaction;
import com.gzoltar.core.runtime.ProbeGroup;

public class Spectrum implements ISpectrum {

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
  public boolean containsProbeGroup(final String probeGroupHash) {
    return this.probeGroups.containsKey(probeGroupHash);
  }

  /**
   * Returns the {@link com.gzoltar.core.runtime.ProbeGroup} with a given name, or null if there is
   * not any.
   */
  public ProbeGroup getProbeGroup(final String probeGroupHash) {
    return this.containsProbeGroup(probeGroupHash) ? this.probeGroups.get(probeGroupHash) : null;
  }

  /**
   * Returns all {@link com.gzoltar.core.runtime.ProbeGroup} that have been registered.
   */
  public List<ProbeGroup> getProbeGroups() {
    return new ArrayList<ProbeGroup>(this.probeGroups.values());
  }

  // === Runtime hitArray ===

  /**
   * Sets a new boolean array for a specific probeGroup.
   * 
   * Note: the probeGroup must exist.
   */
  public boolean[] getHitArray(final String probeGroupHash) {
    if (!this.containsProbeGroup(probeGroupHash)) {
      throw new RuntimeException("ProbeGroup '" + probeGroupHash + "' does not exist!");
    }

    return this.probeGroups.get(probeGroupHash).getHitArray();
  }

  /**
   * Resets the boolean array of a specific probeGroup.
   * 
   * Note: the probeGroup must exist.
   */
  public void resetHitArray(final String probeGroupHash) {
    if (!this.containsProbeGroup(probeGroupHash)) {
      throw new RuntimeException("ProbeGroup '" + probeGroupHash + "' does not exist!");
    }

    this.probeGroups.get(probeGroupHash).resetHitArray();
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
