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
package com.gzoltar.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.runtime.ProbeGroup;

public class Tree implements Iterable<Node> {

  public static final String ROOT_NAME = "root";

  private Node root = null;

  private final Map<String, Node> nodes = new LinkedHashMap<String, Node>();

  /**
   * 
   */
  public Tree() {
    this(true);
  }

  /**
   * 
   * @param createRoot
   */
  public Tree(final boolean createRoot) {
    if (createRoot) {
      this.root = new Node(ROOT_NAME, -1, false, NodeType.PACKAGE, null);
      this.nodes.put(ROOT_NAME, this.root);
    }
  }

  /**
   * 
   * @param node
   */
  public void addNode(final Node node) {
    if (!this.nodes.containsKey(node.getName())) {
      this.nodes.put(node.getName(), node);
    }
  }

  /**
   * 
   * @return
   */
  public Node getRoot() {
    return this.root;
  }

  /**
   * 
   * @param name
   * @return
   */
  public Node getNode(final String name) {
    return this.nodes.get(name);
  }

  /**
   * 
   * @return
   */
  public List<Node> getTargetNodes() {
    return Collections.unmodifiableList(this.getRoot().getLeafNodes());
  }

  /**
   * 
   * @return
   */
  public List<Node> getNodes() {
    return Collections.unmodifiableList(new ArrayList<Node>(this.nodes.values()));
  }

  /**
   * 
   * @param t
   * @return
   */
  public List<Node> getNodesOfType(final NodeType type) {
    List<Node> nodesOfType = new ArrayList<Node>();

    for (Node node : this.nodes.values()) {
      if (node.getNodeType().equals(type)) {
        nodesOfType.add(node);
      }
    }

    return nodesOfType;
  }

  /**
   * 
   * @return
   */
  public int getNumberOfTargetNodes() {
    return this.getRoot().getLeafNodes().size();
  }

  /**
   * 
   * @return
   */
  public int getNumberOfNodes() {
    return this.nodes.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<Node> iterator() {
    return this.nodes.values().iterator();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("~~~~~~~~~~\n");
    Node root = this.getRoot();
    this.toString(stringBuilder, root, "");
    stringBuilder.append("~~~~~~~~~~\n");
    return stringBuilder.toString();
  }

  private void toString(final StringBuilder stringBuilder, final Node node, final String padding) {
    stringBuilder.append(padding + node.toString() + "\n");
    for (Node child : node.getChildren()) {
      this.toString(stringBuilder, child, padding + "  ");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.nodes);
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

    Tree probeGroup = (Tree) obj;

    EqualsBuilder builder = new EqualsBuilder();
    builder.append(this.nodes, probeGroup.nodes);

    return builder.isEquals();
  }
}
