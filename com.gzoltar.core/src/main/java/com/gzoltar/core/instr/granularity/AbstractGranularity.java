package com.gzoltar.core.instr.granularity;

import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.runtime.Collector;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;

public abstract class AbstractGranularity implements IGranularity {

  protected CtClass ctClass;

  protected MethodInfo methodInfo;

  /**
   * 
   * @param ctClass
   * @param methodInfo
   */
  public AbstractGranularity(final CtClass ctClass, final MethodInfo methodInfo) {
    this.ctClass = ctClass;
    this.methodInfo = methodInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Node createNode(final CtClass ctClass, final CtBehavior ctBehavior, final int lineNumber) {
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
}
