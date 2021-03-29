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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Node {

  private String name;

  private final int lineNumber;

  private boolean startBlock;

  private NodeType type;

  private int depth;

  private Node parent;

  private final Map<String, Node> children = new LinkedHashMap<String, Node>();

  private Map<String, Double> suspiciousnessValues = null;

  /**
   * 
   * @param name
   * @param lineNumber
   * @param type
   */
  public Node(final String name, final int lineNumber, final boolean startBlock, final NodeType type) {
    this(name, lineNumber, startBlock, type, null);
  }

  /**
   * 
   * @param name
   * @param type
   * @param parent
   */
  public Node(final String name, final int lineNumber, final boolean startBlock, final NodeType type, final Node parent) {
    this.type = type;
    this.name = name;
    this.lineNumber = lineNumber;
    this.startBlock = startBlock;
    this.setParent(parent);
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
   * @param name
   */
  public void setName(final String name) {
    this.name = name;
  }

  /**
   * 
   * @return
   */
  public String getShortName() {
    if (this.type == NodeType.METHOD) {
      String str = this.name.substring(0, this.name.indexOf('('));
      return str;
    } else {
      return this.getName();
    }
  }

  /**
   * 
   * @return
   */
  public String getNameWithLineNumber() {
    return this.getName()
        + (this.type != NodeType.LINE ? NodeType.LINE.getSymbol() + this.lineNumber : "");
  }

  /**
   * 
   * @return
   */
  public int getLineNumber() {
    return this.lineNumber;
  }

  /**
   * 
   * @return
   */
  public NodeType getNodeType() {
    return this.type;
  }

  /**
   * 
   * @param type
   */
  public void setNodeType(final NodeType type) {
    this.type = type;
  }

  /**
   * 
   * @return
   */
  public boolean isStartBlock() {
    return this.startBlock;
  }

  /**
   * 
   * @param startBlock
   */
  public void setStartBlock(final boolean startBlock) {
    this.startBlock = startBlock;
  }

  /**
   * 
   * @return
   */
  public int getDepth() {
    return this.depth;
  }

  /**
   * 
   * @param parent
   */
  public void setParent(Node parent) {
    this.parent = parent;

    if (this.parent == null) {
      this.depth = 0;
    } else {
      this.depth = this.parent.getDepth() + 1;
      if (!this.parent.children.containsKey(this.name)) {
        this.parent.children.put(this.name, this);
      }
    }
  }

  /**
   * 
   * @return
   */
  public Node getParent() {
    return this.parent;
  }

  /**
   * 
   * @return
   */
  public boolean isRoot() {
    return this.parent == null;
  }

  /**
   * 
   * @return
   */
  public List<Node> getChildren() {
    return new ArrayList<Node>(this.children.values());
  }

  /**
   * 
   * @param name
   * @return
   */
  public Node getChild(final String name) {
    return this.children.get(name);
  }

  /**
   * 
   * @param type
   * @return
   */
  public boolean hasChildrenOfType(final NodeType type) {
    for (Node child : this.children.values()) {
      if (child.type == type) {
        return true;
      }
    }
    return false;
  }

  /**
   * 
   * @return
   */
  public boolean isLeaf() {
    return this.children.isEmpty();
  }

  /**
   * 
   * @return
   */
  public List<Node> getLeafNodes() {
    List<Node> nodes = new ArrayList<Node>();
    this.getLeafNodes(nodes);
    return nodes;
  }

  private void getLeafNodes(final List<Node> nodes) {
    if (this.isLeaf()) {
      nodes.add(this);
    } else {
      for (Node child : this.children.values()) {
        child.getLeafNodes(nodes);
      }
    }
  }

  /**
   * 
   * @param formulaName
   * @param suspiciousnessValue
   */
  public void addSuspiciousnessValue(String formulaName, Double suspiciousnessValue) {
    if (this.suspiciousnessValues == null) {
      this.suspiciousnessValues = new LinkedHashMap<String, Double>();
    }
    this.suspiciousnessValues.put(formulaName, suspiciousnessValue);
  }

  /**
   * 
   * @return
   */
  public boolean hasSuspiciousnessValues() {
    if (this.suspiciousnessValues == null) {
      return false;
    }
    return !this.suspiciousnessValues.isEmpty();
  }

  /**
   * 
   * @return
   */
  public Map<String, Double> getSuspiciousnessValues() {
    return this.suspiciousnessValues;
  }

  /**
   * 
   * @param formulaName
   * @return
   */
  public Double getSuspiciousnessValue(String formulaName) {
    if (this.suspiciousnessValues == null && !this.children.isEmpty()) {
      Double maxSuspiciousnessValue = -1.0 * Double.MIN_VALUE;
      for (Node child : this.children.values()) {
        maxSuspiciousnessValue = Math.max(maxSuspiciousnessValue, child.getSuspiciousnessValue(formulaName));
      }
      return maxSuspiciousnessValue;
    }
    assert this.suspiciousnessValues != null;
    assert this.suspiciousnessValues.containsKey(formulaName);
    return this.suspiciousnessValues.get(formulaName);
  }

  /**
   * 
   * @return
   */
  public int getNumberOfSuspiciousnessValues() {
    assert this.suspiciousnessValues != null;
    return this.suspiciousnessValues.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(this.isLeaf() ? "[probe] " : "");
    sb.append(this.getNameWithLineNumber());

    if (this.hasSuspiciousnessValues()) {
      sb.append("  [ ");
      for (Entry<String, Double> suspiciousness : this.suspiciousnessValues.entrySet()) {
        sb.append(suspiciousness.getKey() + ":" + suspiciousness.getValue() + " ");
      }
      sb.append("]");
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.type);
    builder.append(this.name);
    builder.append(this.lineNumber);
    builder.append(this.depth);
    builder.append(this.parent);
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
    if (!(obj instanceof Node)) {
      return false;
    }

    Node node = (Node) obj;

    EqualsBuilder builder = new EqualsBuilder();

    builder.append(this.type, node.type);
    builder.append(this.name, node.name);
    builder.append(this.lineNumber, node.lineNumber);
    builder.append(this.depth, node.depth);
    builder.append(this.parent, node.parent);

    return builder.isEquals();
  }

}
