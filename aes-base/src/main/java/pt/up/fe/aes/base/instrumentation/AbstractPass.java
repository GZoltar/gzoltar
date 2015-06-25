package pt.up.fe.aes.base.instrumentation;

import java.util.StringTokenizer;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Descriptor;
import pt.up.fe.aes.base.model.Node;
import pt.up.fe.aes.base.runtime.Collector;
import pt.up.fe.aes.base.runtime.ProbeGroup.HitProbe;

public abstract class AbstractPass implements Pass {

	private Node getNode(Collector c, Node parent, String name, Node.Type type) {
		Node node = parent.getChild(name);
		
		if (node == null) {
			node = c.createNode(parent, name, type);
		}
		
		return node;
	}
	
	public Node getNode(CtClass cls) {
		Collector c = Collector.instance();
		Node node = c.getRootNode();
		String tok = cls.getName();

        // Extract Package Hierarchy
        int pkgEnd = tok.lastIndexOf(".");

        if (pkgEnd >= 0) {
            StringTokenizer stok = new StringTokenizer(tok.substring(0, pkgEnd), ".");

            while (stok.hasMoreTokens()) {
                node = getNode(c, node, stok.nextToken(), Node.Type.PACKAGE);
            }
        } 
        else {
            pkgEnd = -1;
        }


        // Extract Class Hierarchy
        StringTokenizer stok = new StringTokenizer(tok.substring(pkgEnd + 1), "$");

        while (stok.hasMoreTokens()) {
            tok = stok.nextToken();
            node = getNode(c, node, tok, Node.Type.CLASS);
        }


        return node;
	}
	
	public Node getNode(CtClass cls, CtBehavior m) {
		Collector c = Collector.instance();
		Node parent = getNode(cls);
		
		//TODO: add method signature
		return getNode(c, parent, m.getName() + Descriptor.toString(m.getSignature()), Node.Type.METHOD);
	}
	
	public Node getNode(CtClass cls, CtBehavior m, int line) {
		Collector c = Collector.instance();
		Node parent = getNode(cls, m);
		
		return getNode(c, parent, String.valueOf(line), Node.Type.LINE);
	}
	
	public HitProbe getHitProbe(CtClass cls, Node n) {
		Collector c = Collector.instance();
		
		return c.createHitProbe(cls.getName(), n.getId());
	}
}
