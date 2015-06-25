package pt.up.fe.aes.base.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Tree implements Iterable<Node> {

	private ArrayList<Node> nodes = new ArrayList<Node> ();

    public Tree () {
        Node node = new Node("root", Node.Type.PACKAGE, nodes.size(), null);

        nodes.add(node);
    }

    public int size() {
        return nodes.size();
    }

    public Node getRoot () {
        return getNode(0);
    }

    public Node getNode (int id) {
        if (id < 0 || id >= nodes.size())
            return null;

        return nodes.get(id);
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    @Override
    public Iterator<Node> iterator () {
        return nodes.iterator();
    }

    public Node addNode (String name, Node.Type type, int parentId) {
        Node parent = getNode(parentId);

        Node child = new Node(name, type, nodes.size(), parent);

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
    	System.out.println(padding + n.getId() + " " + n.getName());
    	for(Node c : n.getChildren()) {
    		print(c, padding + "  ");
    	}
    }
}
