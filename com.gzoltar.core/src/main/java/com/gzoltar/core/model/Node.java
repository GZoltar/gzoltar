package com.gzoltar.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Node implements Serializable {

  private static final long serialVersionUID = 572345681630726180L;

  private final String name;

  private final int lineNumber;

  private final NodeType type;

  private final int depth;

  private final Node parent;

  private final List<Node> children = new ArrayList<Node>();

  private Map<String, Double> suspiciousnessValues = null;

  /**
   * 
   * @param name
   * @param type
   * @param parent
   */
  public Node(final String name, final int lineNumber, final NodeType type, final Node parent) {
    this.type = type;
    this.name = name;
    this.lineNumber = lineNumber;
    this.parent = parent;

    if (this.isRoot()) {
      this.depth = 0;
    } else {
      this.depth = parent.getDepth() + 1;
      parent.children.add(this);
    }
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
   * @return
   */
  public int getDepth() {
    return this.depth;
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
    return this.children;
  }

  /**
   * 
   * @param name
   * @return
   */
  public Node getChild(final String name) {
    for (Node n : this.children) {
      if (n.getName().equals(name)) {
        return n;
      }
    }
    return null;
  }

  /**
   * 
   * @param type
   * @return
   */
  public boolean hasChildrenOfType(final NodeType type) {
    for (Node child : this.children) {
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
      for (Node child : this.children) {
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
      for (Node child : this.children) {
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
