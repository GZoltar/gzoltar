package pt.up.fe.ddu.base.instrumentation.granularity;

import java.util.StringTokenizer;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import pt.up.fe.ddu.base.model.Node;
import pt.up.fe.ddu.base.runtime.Collector;

public abstract class AbstractGranularity implements Granularity {
	
	protected CtClass c;
	protected MethodInfo mi;
	protected CodeIterator ci;

	public AbstractGranularity(CtClass c, MethodInfo mi, CodeIterator ci) {
		this.c = c;
		this.mi = mi;
		this.ci = ci;
	}
	
	private Node getNode(Collector c, Node parent, String name, Node.Type type) {
		Node node = parent.getChild(name);
		
		if (node == null) {
			node = c.createNode(parent, name, type);
		}
		
		return node;
	}
	
	protected Node getNode(CtClass cls) {
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
	
	protected Node getNode(CtClass cls, CtBehavior m) {
		Collector c = Collector.instance();
		Node parent = getNode(cls);
		
		return getNode(c, parent, m.getName() + Descriptor.toString(m.getSignature()), Node.Type.METHOD);
	}
	
	public Node getNode(CtClass cls, CtBehavior m, int line) {
		Collector c = Collector.instance();
		Node parent = getNode(cls, m);
		
		return getNode(c, parent, String.valueOf(line), Node.Type.LINE);
	}
	
}