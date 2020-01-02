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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;
import javassist.CtBehavior;

public final class Probe {

  private final int arrayIndex;

  private final Node node;

  private final CtBehavior ctBehavior;

  /**
   * Creates a new {@link com.gzoltar.core.runtime.Probe} object.
   * 
   * @param arrayIndex
   * @param node
   */
  public Probe(final int arrayIndex, final Node node, final CtBehavior ctBehavior) {
    this.arrayIndex = arrayIndex;
    this.node = node;
    this.ctBehavior = ctBehavior;
  }

  /**
   * Returns the array index.
   */
  public int getArrayIndex() {
    return this.arrayIndex;
  }

  /**
   * Returns the correspondent {@link com.gzoltar.core.model.Node} object.
   */
  public Node getNode() {
    return this.node;
  }

  /**
   * Returns the correspondent {@link javassist.CtBehavior} object of a probe.
   */
  public CtBehavior getCtBehavior() {
    return this.ctBehavior;
  }

  /**
   * Returns true if this probe has been injected in a class initialiser (static initialiser).
   * 
   * @return <code>true</code> if this probe has been injected in a class initialiser, false
   *         otherwise.
   */
  public boolean isProbeInClassInitialiser() {
    return this.ctBehavior.getMethodInfo2().isStaticInitializer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[Probe] ");
    sb.append("[");
    sb.append(this.arrayIndex);
    sb.append("] ");
    sb.append(this.node.toString());
    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.arrayIndex);
    builder.append(this.node);
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
    if (!(obj instanceof Probe)) {
      return false;
    }

    Probe probe = (Probe) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(this.arrayIndex, probe.arrayIndex);
    builder.append(this.node, probe.node);

    return builder.isEquals();
  }
}
