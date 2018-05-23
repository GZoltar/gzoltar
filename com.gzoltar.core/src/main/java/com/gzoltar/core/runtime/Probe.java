package com.gzoltar.core.runtime;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.model.Node;

public final class Probe {

  private final int arrayIndex;

  private final Node node;

  /**
   * Creates a new {@link com.gzoltar.core.runtime.Probe} object.
   * 
   * @param arrayIndex
   * @param node
   */
  public Probe(int arrayIndex, Node node) {
    this.arrayIndex = arrayIndex;
    this.node = node;
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
