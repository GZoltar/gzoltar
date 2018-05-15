package com.gzoltar.core.model;

import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Descriptor;

public final class NodeFactory {

  /**
   * Create a {@link com.gzoltar.core.model.Node} object and its ancestors, if not found.
   * 
   * @param ctClass
   * @param ctBehavior
   * @param lineNumber
   * @return An object of {@link com.gzoltar.core.model.Node}
   */
  public static Node createNode(final CtClass ctClass, final CtBehavior ctBehavior, final int lineNumber) {
    Tree tree = Collector.instance().getSpectrumListener().getSpectrum().getTree();

    String packageName = ctClass.getPackageName() == null ? "" : ctClass.getPackageName();
    Node packageNode = tree.getNode(packageName);
    if (packageNode == null) {
      packageNode = Collector.instance().createNode(tree.getRoot(), packageName, lineNumber,
          NodeType.PACKAGE);
    }

    StringBuilder className = new StringBuilder(packageName);
    className.append(NodeType.CLASS.getSymbol());
    className.append(ctClass.getSimpleName());
    String classNameStr = className.toString();
    Node classNode = tree.getNode(classNameStr);
    if (classNode == null) {
      classNode =
          Collector.instance().createNode(packageNode, classNameStr, lineNumber, NodeType.CLASS);
    }

    StringBuilder methodName = className;
    methodName.append(NodeType.METHOD.getSymbol());
    methodName.append(ctBehavior.getName());
    methodName.append(Descriptor.toString(ctBehavior.getSignature()));
    String methodNameStr = methodName.toString();
    Node methodNode = tree.getNode(methodNameStr);
    if (methodNode == null) {
      methodNode =
          Collector.instance().createNode(classNode, methodNameStr, lineNumber, NodeType.METHOD);
    }

    StringBuilder lineName = methodName;
    lineName.append(NodeType.LINE.getSymbol());
    lineName.append(String.valueOf(lineNumber));
    String lineNameStr = lineName.toString();
    Node lineNode = tree.getNode(lineNameStr);
    if (lineNode == null) {
      lineNode =
          Collector.instance().createNode(methodNode, lineNameStr, lineNumber, NodeType.LINE);
    }

    return lineNode;
  }

  /**
   * Parse the name of a {@link com.gzoltar.core.model.Node} and create its ancestors and the node
   * itself, if not found.
   * 
   * @param tree
   * @param nodeName Name of a node
   * @param nodeType
   * @return An object of {@link com.gzoltar.core.model.Node}
   */
  public static Node createNode(final Tree tree, final String nodeName, final NodeType nodeType) {

    final int lineNumber = Integer.valueOf(
        nodeName.substring(nodeName.indexOf(NodeType.LINE.getSymbol()) + 1, nodeName.length()));

    // === Package ===

    final String packageName = nodeName.substring(0,
        nodeType.equals(NodeType.PACKAGE) ? nodeName.indexOf(NodeType.LINE.getSymbol())
            : nodeName.indexOf(NodeType.CLASS.getSymbol()));
    Node packageNode = tree.getNode(packageName);
    if (packageNode == null) {
      packageNode = new Node(packageName, lineNumber, NodeType.PACKAGE, tree.getRoot());
    }

    if (nodeType.equals(NodeType.PACKAGE)) {
      return packageNode;
    }
    tree.addNode(packageNode);

    // === Class ===

    final String className = nodeName.substring(0,
        nodeType.equals(NodeType.CLASS) ? nodeName.indexOf(NodeType.LINE.getSymbol())
            : nodeName.indexOf(NodeType.METHOD.getSymbol()));
    Node classNode = tree.getNode(className);
    if (classNode == null) {
      classNode = new Node(className, lineNumber, NodeType.CLASS, packageNode);
    }

    if (nodeType.equals(NodeType.CLASS)) {
      return classNode;
    }
    tree.addNode(classNode);

    // === Method ===

    final String methodName = nodeName.substring(0, nodeName.indexOf(NodeType.LINE.getSymbol()));
    Node methodNode = tree.getNode(methodName);
    if (methodNode == null) {
      methodNode = new Node(methodName, lineNumber, NodeType.METHOD, classNode);
    }

    if (nodeType.equals(NodeType.METHOD)) {
      return methodNode;
    }
    tree.addNode(methodNode);

    // === Line ===

    final String lineName = nodeName;
    Node lineNode = tree.getNode(lineName);
    if (lineNode == null) {
      lineNode = new Node(lineName, lineNumber, NodeType.LINE, methodNode);
    }

    return lineNode;
  }

}
