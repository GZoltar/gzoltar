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
package com.gzoltar.core.runtime;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;
import javassist.CtBehavior;
import javassist.CtClass;

public final class ProbeGroup {

  private final String hash;

  private final CtClass ctClass;

  private final List<Probe> probes;

  /**
   * Constructs a new ProbeGroup.
   * 
   * @param hash
   * @param ctClass
   */
  public ProbeGroup(String hash, CtClass ctClass) {
    this(hash, ctClass, new ArrayList<Probe>());
  }

  /**
   * Constructs a new ProbeGroup.
   * 
   * @param hash
   * @param ctClass
   * @param probes
   */
  public ProbeGroup(String hash, CtClass ctClass, List<Probe> probes) {
    this.hash = hash;
    this.ctClass = ctClass;
    this.probes = probes;
  }

  /**
   * Returns the MD5 hash of the bytecode of the class under test.
   */
  public String getHash() {
    return this.hash;
  }

  /**
   * Returns the correspondent {@link javassist.CtClass} object of a probeGroup.
   */
  public CtClass getCtClass() {
    return this.ctClass;
  }

  /**
   * Returns the name of a probeGroup.
   */
  public String getName() {
    return this.ctClass.getName();
  }

  // === Probes ===

  /**
   * Registers a new {@link com.gzoltar.core.runtime.Probe} object.
   */
  public Probe registerProbe(final Node node, final CtBehavior ctBehavior) {
    Probe probe = this.findProbeByNode(node);
    if (probe == null) {
      probe = new Probe(this.probes.size(), node, ctBehavior);
      this.probes.add(probe);
    }
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
    sb.append(this.getName());

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
    builder.append(this.getName());
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
    builder.append(this.getName(), probeGroup.getName());
    builder.append(this.probes, probeGroup.probes);

    return builder.isEquals();
  }

}
