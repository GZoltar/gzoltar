package com.gzoltar.core.runtime;

import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;

public final class ProbeGroup {

  private final String name;

  private final Set<Probe> probes;

  private boolean[] hitArray = null;

  /**
   * 
   * @param name
   */
  public ProbeGroup(String name) {
    this.name = name;
    this.probes = new LinkedHashSet<Probe>();
  }

  /**
   * 
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * 
   * @param node
   */
  public Probe registerProbe(Node node) {
    Probe probe = new Probe(this.probes.size(), node);
    this.probes.add(probe);
    return probe;
  }

  /**
   * 
   * @return
   */
  public Set<Probe> getProbes() {
    return this.probes;
  }

  /**
   * 
   * @return
   */
  public int getNumberOfProbes() {
    return this.probes.size();
  }

  /**
   * 
   * @return
   */
  public boolean hasHitArray() {
    return this.hitArray != null;
  }

  /**
   * 
   * @return
   */
  public boolean[] getHitArray() {
    if (this.hitArray == null) {
      this.hitArray = new boolean[this.probes.size()];
    }
    return this.hitArray;
  }

  /**
   * 
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
   * 
   * @return
   */
  public Set<Node> getHitNodes() {
    Set<Node> hitNodes = new LinkedHashSet<Node>();

    for (Probe probe : this.probes) {
      if (this.hitArray[probe.getArrayIndex()]) {
        hitNodes.add(probe.getNode());
      }
    }

    return hitNodes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "Name: " + this.name + " has " + this.probes.size() + " probes. HitArray: "
        + this.hitArray;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
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
    builder.append(this.name, probeGroup.name);
    builder.append(this.probes, probeGroup.probes);
    builder.append(this.hitArray, probeGroup.hitArray);

    return builder.isEquals();
  }
}
