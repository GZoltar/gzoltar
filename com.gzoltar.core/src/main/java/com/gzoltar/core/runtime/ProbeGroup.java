package com.gzoltar.core.runtime;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;

public final class ProbeGroup {

  private final String hash;

  private final String name;

  private final List<Probe> probes;

  /**
   * Constructs a new ProbeGroup.
   * 
   * @param name
   */
  public ProbeGroup(String hash, String name) {
    this(hash, name, new ArrayList<Probe>());
  }

  /**
   * Constructs a new ProbeGroup.
   * 
   * @param name
   * @param probes
   */
  public ProbeGroup(String hash, String name, List<Probe> probes) {
    this.hash = hash;
    this.name = name;
    this.probes = probes;
  }

  /**
   * Returns the MD5 hash of the bytecode of the class under test.
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Returns the name of a probeGroup.
   */
  public String getName() {
    return this.name;
  }

  // === Probes ===

  /**
   * Registers a new {@link com.gzoltar.core.runtime.Probe} object.
   */
  public Probe registerProbe(Node node) {
    Probe probe = new Probe(this.probes.size(), node);
    this.probes.add(probe);
    return probe;
  }

  /**
   * Returns all {@link com.gzoltar.core.runtime.Probe} objects that have been registered.
   */
  public List<Probe> getProbes() {
    return this.probes;
  }

  /**
   * Returns the number of {@link com.gzoltar.core.runtime.Probe} objects of a probeGroup.
   */
  public int getNumberOfProbes() {
    return this.probes.size();
  }

  /**
   * Returns true if a probeGroup does not contain any {@link com.gzoltar.core.runtime.Probe}
   * object.
   */
  public boolean isEmpty() {
    return this.probes.isEmpty();
  }

  /**
   * Return the {@link com.gzoltar.core.runtime.Probe} object that contains a specific
   * {@link com.gzoltar.core.model.Node} object, null otherwise.
   */
  public Probe findProbeByNode(Node node) {
    for (Probe probe : this.probes) {
      if (probe.getNode().equals(node)) {
        return probe;
      }
    }
    return null;
  }

  // === Nodes ===

  /**
   * Returns all {@link com.gzoltar.core.model.Node} object that belong to a probeGroup.
   */
  public List<Node> getNodes() {
    List<Node> nodes = new ArrayList<Node>();
    for (Probe probe : this.probes) {
      nodes.add(probe.getNode());
    }
    return nodes;
  }

  /**
   * Returns a {@link com.gzoltar.core.model.Node} object with a specific name, or null there is not
   * any.
   */
  public Node getNode(String nodeName) {
    for (Probe probe : this.probes) {
      if (probe.getNode().getName().equals(nodeName)) {
        return probe.getNode();
      }
    }
    return null;
  }

  // === Overrides ===

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ProbeGroup] ");
    sb.append(this.hash);
    sb.append(" | ");
    sb.append(this.name);

    for (Probe probe : this.probes) {
      sb.append("\n");
      sb.append(probe.toString());
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.hash);
    builder.append(this.name);
    builder.append(this.probes);
    return builder.toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof ProbeGroup)) {
      return false;
    }

    ProbeGroup probeGroup = (ProbeGroup) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(this.hash, probeGroup.hash);
    builder.append(this.name, probeGroup.name);
    builder.append(this.probes, probeGroup.probes);

    return builder.isEquals();
  }

}
