/**
 * Copyright (C) 2020 GZoltar contributors.
 * 
 * This file is part of GZoltar.
 * 
 * GZoltar is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * GZoltar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GZoltar. If
 * not, see <https://www.gnu.org/licenses/>.
 */
package com.gzoltar.core.model;

import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Descriptor;

public final class NodeFactory {

  /**
   * Create a {@link com.gzoltar.core.model.Node} object.
   * 
   * @param ctClass
   * @param ctBehavior
   * @param lineNumber
   * @return An {@link com.gzoltar.core.model.Node} object
   */
  public static Node createNode(final CtClass ctClass, final CtBehavior ctBehavior,
      final int lineNumber, final boolean isNewBlock) {

    String packageName = ctClass.getPackageName() == null ? "" : ctClass.getPackageName();

    StringBuilder className = new StringBuilder(packageName);
    className.append(NodeType.CLASS.getSymbol());
    className.append(ctClass.getSimpleName());

    StringBuilder methodName = className;
    methodName.append(NodeType.METHOD.getSymbol());
    methodName.append(ctBehavior.getName());
    methodName.append(Descriptor.toString(ctBehavior.getSignature()));

    StringBuilder lineName = methodName;
    lineName.append(NodeType.LINE.getSymbol());
    lineName.append(String.valueOf(lineNumber));

    return new Node(lineName.toString(), lineNumber, isNewBlock, NodeType.LINE);
  }

  /**
   * Parse a {@link com.gzoltar.core.model.Node} object and create its ancestors.
   * @param tree
   * @param node
   */
  public static void createNode(final Tree tree, final Node node) {

    final String nodeName = node.getName();
    final NodeType nodeType = node.getNodeType();
    final int lineNumber = node.getLineNumber();
    final boolean startBlock = node.isStartBlock();

    // === Package ===

    final String packageName = nodeName.substring(0,
        nodeType.equals(NodeType.PACKAGE) ? nodeName.indexOf(NodeType.LINE.getSymbol())
            : nodeName.indexOf(NodeType.CLASS.getSymbol()));
    Node packageNode = tree.getNode(packageName);
    if (packageNode == null) {
      packageNode = new Node(packageName, lineNumber, startBlock, NodeType.PACKAGE, tree.getRoot());
      tree.addNode(packageNode);
    }

    // === Class ===

    if (nodeType.equals(NodeType.CLASS)) {
      node.setParent(packageNode);
      tree.addNode(node);
      return;
    }

    final String className = nodeName.substring(0,
        nodeType.equals(NodeType.CLASS) ? nodeName.indexOf(NodeType.LINE.getSymbol())
            : nodeName.indexOf(NodeType.METHOD.getSymbol()));
    Node classNode = tree.getNode(className);
    if (classNode == null) {
      classNode = new Node(className, lineNumber, startBlock, NodeType.CLASS, packageNode);
      tree.addNode(classNode);
    }

    // === Method ===

    if (nodeType.equals(NodeType.METHOD)) {
      node.setParent(classNode);
      tree.addNode(node);
      return;
    }

    final String methodName = nodeName.substring(0, nodeName.indexOf(NodeType.LINE.getSymbol()));
    Node methodNode = tree.getNode(methodName);
    if (methodNode == null) {
      methodNode = new Node(methodName, lineNumber, startBlock, NodeType.METHOD, classNode);
      tree.addNode(methodNode);
    }

    // === Line ===

    node.setParent(methodNode);
    tree.addNode(node);
  }

}
