package com.gzoltar.core.model;

import java.util.ArrayList;
import java.util.List;

public class Node {

  private final NodeType type;

  private final String name;

  private final int id;

  private final int depth;

  private final Node parent;

  private final List<Node> children = new ArrayList<Node>();

  public Node(final String name, final NodeType type, final int id, final Node parent) {
    this.type = type;
    this.name = name;
    this.id = id;
    this.parent = parent;

    if (isRoot()) {
      this.depth = 0;
    } else {
      this.depth = parent.getDepth() + 1;
      parent.addChild(this);
    }
  }

  private void addChild(final Node node) {
    this.children.add(node);
  }

  public int getDepth() {
    return this.depth;
  }

  private boolean isRoot() {
    return this.parent == null;
  }

  public boolean isLeaf() {
    return this.children.isEmpty();
  }

  public Node getChild(final String name) {
    for (Node n : this.children) {
      if (n.getName().equals(name)) {
        return n;
      }
    }
    return null;
  }

  public Node getParent() {
    return this.parent;
  }

  public int getParentId() {
    if (this.isRoot()) {
      return -1;
    }
    return this.parent.id;
  }

  public String getName() {
    return this.name;
  }

  public int getId() {
    return this.id;
  }

  public NodeType getType() {
    return this.type;
  }

  public List<Node> getChildren() {
    return this.children;
  }

  public String toString() {
    return "{\"n\":\"" + this.getName() + "\",\"p\":" + this.getParentId() + "}";
  }

  public Node getNodeOfType(NodeType type) {
    if (this.type == type) {
      return this;
    } else if (this.parent != null) {
      return this.parent.getNodeOfType(type);
    } else {
      return null;
    }
  }

  public List<Node> getLeafNodes() {
    List<Node> nodes = new ArrayList<Node>();
    this.getLeafNodes(nodes);
    return nodes;
  }

  private void getLeafNodes(final List<Node> nodes) {
    if (this.isLeaf()) {
      nodes.add(this);
    } else {
      for (Node c : this.children) {
        c.getLeafNodes(nodes);
      }
    }
  }

  public String getShortName() {
    if (this.type == NodeType.METHOD) {
      String str = this.name.substring(0, this.name.indexOf('('));
      return str;
    } else {
      return this.getName();
    }
  }

  public String getFullName() {
    Node p = this.getParent();

    if (p == null || p.isRoot()) {
      return name;
    }

    return p.getFullName() + this.getSymbol(p.type, type) + name;
  }

  private String getSymbol(final NodeType t1, final NodeType t2) {
    if (t1 == NodeType.PACKAGE) {
      return t1.getSymbol();
    } else {
      return t2.getSymbol();
    }
  }

  public boolean hasChildrenOfType(final NodeType t) {
    for (Node child : this.children) {
      if (child.type == t) {
        return true;
      }
    }
    return false;
  }

}
