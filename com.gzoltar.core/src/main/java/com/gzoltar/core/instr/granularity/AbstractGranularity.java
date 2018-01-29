package com.gzoltar.core.instr.granularity;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.runtime.Collector;
import com.gzoltar.core.util.ClassUtils;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public abstract class AbstractGranularity implements IGranularity {

  protected CtClass ctClass;

  protected MethodInfo methodInfo;

  private static final Collector COLLECTOR = Collector.instance();

  /**
   * 
   * @param ctClass
   * @param methodInfo
   */
  public AbstractGranularity(final CtClass ctClass, final MethodInfo methodInfo) {
    this.ctClass = ctClass;
    this.methodInfo = methodInfo;
  }

  private Node getNode(final Node parent, final String fullName) {
    return parent.getChild(fullName);
  }

  private Node createNode(final Node parent, final String name, final int lineNumber,
      final NodeType nodeType) {
    return COLLECTOR.createNode(parent, name, lineNumber, nodeType);
  }

  /**
   * 
   * @param ctClass
   * @param lineNumber
   * @return
   */
  protected Node getNode(final CtClass ctClass, final int lineNumber) {
    // a class could be a child of a package or a child of another class
    Node parent = null;
    if (ClassUtils.isAnonymousClass(ctClass)) {
      try {
        parent = this.getNode(ctClass.getSuperclass(), lineNumber);
      } catch (NotFoundException e) {
        throw new RuntimeException(e);
      }
    } else {
      String packageName = ctClass.getPackageName() == null ? "" : ctClass.getPackageName();
      parent = this.getNode(COLLECTOR.getRootNode(), packageName);
      if (parent == null) {
        // package is not available yet, therefore create one
        parent =
            this.createNode(COLLECTOR.getRootNode(), packageName, 1, NodeType.PACKAGE);
      }
    }

    String className = parent.getName() + NodeType.CLASS.getSymbol() + ctClass.getSimpleName();
    Node node = this.getNode(parent, className);
    if (node == null) {
      node = this.createNode(parent, className, lineNumber, NodeType.CLASS);
    }

    return node;
  }

  /**
   * 
   * @param ctBehavior
   * @param lineNumber
   * @return
   */
  protected Node getNode(final CtBehavior ctBehavior, final int lineNumber) {
    Node parent = this.getNode(ctBehavior.getDeclaringClass(), lineNumber);

    String methodName = parent.getName() + NodeType.METHOD.getSymbol() + ctBehavior.getName()
        + Descriptor.toString(ctBehavior.getSignature());
    Node node = this.getNode(parent, methodName);
    if (node == null) {
      node = this.createNode(parent, methodName, lineNumber, NodeType.METHOD);
    }

    return node;
  }

  /**
   * {@inheritDoc}
   */
  public Node getNode(final CtClass ctClass, final CtBehavior ctBehavior, final int lineNumber) {
    Node parent = this.getNode(ctBehavior, lineNumber);

    String lineName = parent.getName() + NodeType.LINE.getSymbol() + String.valueOf(lineNumber);
    Node node = this.getNode(parent, lineName);
    if (node == null) {
      node = this.createNode(parent, lineName, lineNumber, NodeType.LINE);
    }

    return node;
  }
}
