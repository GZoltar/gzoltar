package com.gzoltar.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.gzoltar.core.runtime.ProbeGroup;

public class Tree implements Iterable<Node> {

  public static final String ROOT_NAME = "root";

  private final List<Node> nodes = new ArrayList<Node>();

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
      Node node = new Node(ROOT_NAME, -1, NodeType.PACKAGE, null);
      this.nodes.add(node);
    }
  }

  /**
   * 
   * @param node
   */
  public void addNode(final Node node) {
    this.nodes.add(node);
  }

  /**
   * 
   * @return
   */
  public Node getRoot() {
    return this.nodes.get(0);
  }

  /**
   * 
   * @param name
   * @return
   */
  public Node getNode(final String name) {
    for (Node node : this.nodes) {
      if (node.getName().equals(name)) {
        return node;
      }
    }
    return null;
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
    return Collections.unmodifiableList(this.nodes);
  }

  /**
   * 
   * @param t
   * @return
   */
  public List<Node> getNodesOfType(final NodeType type) {
    List<Node> nodesOfType = new ArrayList<Node>();

    for (Node node : this.nodes) {
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
    return this.nodes.iterator();
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
