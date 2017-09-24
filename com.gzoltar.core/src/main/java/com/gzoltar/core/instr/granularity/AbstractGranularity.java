package com.gzoltar.core.instr.granularity;

import java.util.StringTokenizer;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public abstract class AbstractGranularity implements IGranularity {

  protected CtClass ctClass;
  protected MethodInfo methodInfo;
  protected CodeIterator codeIterator;

  public AbstractGranularity(final CtClass ctClass, final MethodInfo methodInfo,
      final CodeIterator codeIterator) {
    this.ctClass = ctClass;
    this.methodInfo = methodInfo;
    this.codeIterator = codeIterator;
  }

  private Node getNode(final Collector collector, final Node parent, final String name,
      final NodeType nodeType) {
    Node node = parent.getChild(name);

    if (node == null) {
      node = collector.createNode(parent, name, nodeType);
    }

    return node;
  }

  protected Node getNode(final CtClass ctClass) {
    Collector collector = Collector.instance();
    Node node = collector.getRootNode();
    String tok = ctClass.getName();

    // Extract Package Hierarchy
    int pkgEnd = tok.lastIndexOf(".");

    if (pkgEnd >= 0) {
      StringTokenizer stok = new StringTokenizer(tok.substring(0, pkgEnd), ".");

      while (stok.hasMoreTokens()) {
        node = this.getNode(collector, node, stok.nextToken(), NodeType.PACKAGE);
      }
    } else {
      pkgEnd = -1;
    }

    // Extract Class Hierarchy
    StringTokenizer stok = new StringTokenizer(tok.substring(pkgEnd + 1), "$");

    while (stok.hasMoreTokens()) {
      tok = stok.nextToken();
      node = this.getNode(collector, node, tok, NodeType.CLASS);
    }

    return node;
  }

  protected Node getNode(final CtClass ctClass, final CtBehavior ctBehavior) {
    Collector c = Collector.instance();
    Node parent = this.getNode(ctClass);

    return getNode(c, parent, ctBehavior.getName() + Descriptor.toString(ctBehavior.getSignature()),
        NodeType.METHOD);
  }

  public Node getNode(final CtClass ctClass, final CtBehavior ctBehavior, final int line) {
    Collector c = Collector.instance();
    Node parent = this.getNode(ctClass, ctBehavior);

    return this.getNode(c, parent, String.valueOf(line), NodeType.LINE);
  }

}
