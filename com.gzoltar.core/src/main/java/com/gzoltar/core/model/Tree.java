package com.gzoltar.core.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.gzoltar.core.model.Node.Type;

public class Tree implements Iterable<Node> {

  private ArrayList<Node> nodes = new ArrayList<Node>();

  public Tree() {
    this(true);
  }

  public Tree(boolean createRoot) {
    if (createRoot) {
      Node node = new Node("root", Node.Type.PACKAGE, nodes.size(), null);
      nodes.add(node);
    }
  }

  public int size() {
    return nodes.size();
  }

  public Node getRoot() {
    return getNode(0);
  }

  public Node getNode(int id) {
    if (id < 0 || id >= nodes.size())
      return null;

    return nodes.get(id);
  }

  public List<Node> getNodes() {
    return Collections.unmodifiableList(nodes);
  }

  @Override
  public Iterator<Node> iterator() {
    return nodes.iterator();
  }

  public Node addNode(String name, Node.Type type, int parentId) {
    return addNode(name, type, nodes.size(), parentId);
  }

  public Node addNode(String name, Node.Type type, int id, int parentId) {
    Node parent = getNode(parentId);

    Node child = new Node(name, type, id, parent);

    nodes.add(child);
    return child;
  }

  public void print() {
    System.out.println("~~~~~~~~~~");
    Node n = getRoot();
    print(n, "");
    System.out.println("~~~~~~~~~~");
  }

  private void print(Node n, String padding) {
    System.out.println(padding + n.getId() + " " + n.getFullName());
    for (Node c : n.getChildren()) {
      print(c, padding + "  ");
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder("\"tree\":[");
    int i = 0;
    for (Node n : nodes) {
      if (i++ != 0) {
        sb.append(",");
      }
      sb.append(n.toString());
    }
    sb.append("]");
    return sb.toString();
  }

  public List<Node> getNodesOfType(Type t) {
    List<Node> nodesOfType = new ArrayList<Node>();

    for (Node node : nodes) {
      if (node.getType() == t)
        nodesOfType.add(node);
    }

    return nodesOfType;
  }

  public Node findNode(String name) {
    for (Node node : nodes) {
      if (name.equals(node.getFullName())) {
        return node;
      }
    }
    return null;
  }
}
