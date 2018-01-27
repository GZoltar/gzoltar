package com.gzoltar.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Tree implements Iterable<Node> {

  private final List<Node> nodes = new ArrayList<Node>();

  public Tree() {
    this(true);
  }

  public Tree(final boolean createRoot) {
    if (createRoot) {
      Node node = new Node("root", NodeType.PACKAGE, nodes.size(), null);
      this.nodes.add(node);
    }
  }

  public int size() {
    return this.nodes.size();
  }

  public Node getRoot() {
    return this.getNode(0);
  }

  public Node getNode(final int id) {
    if (id < 0 || id >= this.nodes.size()) {
      return null;
    }
    return this.nodes.get(id);
  }

  public List<Node> getNodes() {
    return Collections.unmodifiableList(this.nodes);
  }

  @Override
  public Iterator<Node> iterator() {
    return this.nodes.iterator();
  }

  public Node addNode(final String name, final NodeType type, final int parentId) {
    return this.addNode(name, type, this.nodes.size(), parentId);
  }

  public Node addNode(final String name, final NodeType type, final int id, final int parentId) {
    Node parent = this.getNode(parentId);
    Node child = new Node(name, type, id, parent);
    this.nodes.add(child);
    return child;
  }

  public void print() {
    System.out.println("~~~~~~~~~~");
    Node n = this.getRoot();
    this.print(n, "");
    System.out.println("~~~~~~~~~~");
  }

  private void print(final Node n, final String padding) {
    System.out.println(padding + n.getId() + " " + n.getFullName());
    for (Node c : n.getChildren()) {
      this.print(c, padding + "  ");
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("\"tree\":[");
    int i = 0;
    for (Node n : this.nodes) {
      if (i++ != 0) {
        sb.append(",");
      }
      sb.append(n.toString());
    }
    sb.append("]");
    return sb.toString();
  }

  public List<Node> getNodesOfType(final NodeType t) {
    List<Node> nodesOfType = new ArrayList<Node>();

    for (Node node : this.nodes) {
      if (node.getType() == t) {
        nodesOfType.add(node);
      }
    }

    return nodesOfType;
  }

  public Node findNode(final String name) {
    for (Node node : this.nodes) {
      if (name.equals(node.getFullName())) {
        return node;
      }
    }
    return null;
  }

}
