package pt.up.fe.aes.base.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public static enum Type {
		PACKAGE("."),
		CLASS("$"),
		METHOD("#"),
		LINE(":");

		private final String symbol;

		private Type(String symbol) {
			this.symbol = symbol;
		}

		public String getSymbol () {
			return symbol;
		}
	}

	private Type type;
	private String name;
	private int id;
	private int depth;
	private Node parent;
	private List<Node> children = new ArrayList<Node> ();

	Node (String name, Type type, int id, Node parent) {
		this.type = type;
		this.name = name;
		this.id = id;
		this.parent = parent;

		if (isRoot())
			this.depth = 0;
		else {
			this.depth = parent.getDepth() + 1;
			parent.addChild(this);
		}
	}

	private void addChild(Node node) {
		children.add(node);
	}

	private int getDepth() {
		return this.depth;
	}

	private boolean isRoot() {
		return parent == null;
	}
	
	public boolean isLeaf() {
		return children.isEmpty();
	}

	public Node getChild(String name) {
		for (Node n : children) {
			if(n.getName().equals(name)) {
				return n;
			}
		}
		
		return null;
	}
	
	public Node getParent() {
		return this.parent;
	}

	public int getParentId() {
		if (isRoot()) {
			return -1;
		}
		
		return this.parent.id;
	}
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public Type getType() {
		return type;
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public String toString() {
		return "{\"n\":\"" + getName() + "\",\"p\":" + getParentId() + "}";
	}
	
	public Node getNodeOfType(Type type) {
		if (this.type == type) {
			return this;
		}
		else if (this.parent != null) {
			return this.parent.getNodeOfType(type);
		}
		else {
			return null;
		}
	}
	
	public List<Node> getLeafNodes() {
		List<Node> nodes = new ArrayList<Node>();
		getLeafNodes(nodes);
		return nodes;
	}

	private void getLeafNodes(List<Node> nodes) {
		if (isLeaf()) {
			nodes.add(this);
		}
		else {
			for(Node c : children) {
				c.getLeafNodes(nodes);
			}
		}
	}

	public String getFullName() {
		Node p = getParent();

        if (p == null || p.isRoot())
            return name;

        return p.getFullName() + getSymbol(p.type, type) + name;
	}
	
	private static String getSymbol(Type t1, Type t2) {
		if (t1 == Type.PACKAGE) {
			return t1.getSymbol();
		}
		else return t2.getSymbol();
	}
	
	public boolean hasChildrenOfType(Type t) {
		for (Node child : children) {
			if(child.type == t) {
				return true;
			}
		}
		return false;
	}
}
