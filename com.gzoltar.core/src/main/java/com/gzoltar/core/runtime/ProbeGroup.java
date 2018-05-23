package com.gzoltar.core.runtime;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;

public final class ProbeGroup implements Cloneable {

  private final String hash;

  private final String name;

  private final List<Probe> probes;

  private boolean[] hitArray = null;

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
   * Registers a new  {@link com.gzoltar.core.runtime.Probe} object.
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
   * 
   */
  public Node getNode(String nodeName) {
    for (Probe probe : this.probes) {
      if (probe.getNode().getName().equals(nodeName)) {
        return probe.getNode();
      }
    }
    return null;
  }

  /**
   * Returns all executed {@link com.gzoltar.core.model.Node} objects.
   */
  public List<Node> getHitNodes() {
    List<Node> hitNodes = new ArrayList<Node>();
    if (this.hitArray == null) {
      // When a class is serialised / deserialised an instance of a class could be created only
      // using metadata of that class, i.e., there is a chance that none of "normal" constructors or
      // even static constructors is called to construct that class, and none of the lines of code
      // is actually executed. For those particular cases, GZoltar would instrument a class as
      // normal and some probes would be created, however as no constructor (either "normal" or
      // static) is called, the hitArray is never initialised.
      return hitNodes;
    }
    assert this.hitArray.length == this.probes.size();

    for (Probe probe : this.probes) {
      if (this.hitArray[probe.getArrayIndex()]) {
        Node node = probe.getNode();
        node.setHasBeenExecuted();
        hitNodes.add(node);
      }
    }

    return hitNodes;
  }

  /**
   * Returns true if a {@link com.gzoltar.core.model.Node} at index has been executed, false
   * otherwise.
   * 
   * @param index
   * @return
   */
  public boolean hitNode(int index) {
    if (this.hitArray == null) {
      return false;
    }
    return this.hitArray[index];
  }

  // === HitArray ===

  /**
   * Returns true if there is an hitArray, false otherwise.
   */
  public boolean hasHitArray() {
    return this.hitArray != null;
  }

  /**
   * Returns the hitArray of a probeGroup.
   */
  public boolean[] getHitArray() {
    if (this.hitArray == null) {
      this.hitArray = new boolean[this.probes.size()];
    }
    return this.hitArray;
  }

  /**
   * Resets the hitArray of a probeGroup.
   */
  public void resetHitArray() {
    if (this.hitArray == null) {
      return;
    }

    for (int i = 0; i < this.hitArray.length; i++) {
      this.hitArray[i] = false;
    }
  }

  /**
   * Replace any existing hitArray with a new one.
   */
  public void setHitArray(boolean[] hitArray) {
    this.hitArray = hitArray;
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
    builder.append(this.hitArray);
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
    builder.append(this.hitArray, probeGroup.hitArray);

    return builder.isEquals();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    ProbeGroup clone = new ProbeGroup(this.hash, this.name, this.probes);

    if (this.hitArray != null) {
      boolean[] hitArrayClone = new boolean[this.hitArray.length];
      System.arraycopy(this.hitArray, 0, hitArrayClone, 0, this.hitArray.length);
      clone.hitArray = hitArrayClone;
    }

    return clone;
  }
}
