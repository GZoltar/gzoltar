package pt.up.fe.aes.base.model;

import java.util.ArrayList;
import java.util.List;

public class Node {
	public static enum Type {
		PACKAGE("."),
		CLASS("$"),
		METHOD("!"),
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

	public Node getChild(String name) {
		for (Node n : children) {
			if(n.getName().equals(name)) {
				return n;
			}
		}
		
		return null;
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

}
