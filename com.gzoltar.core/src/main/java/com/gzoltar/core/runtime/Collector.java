package com.gzoltar.core.runtime;

import com.gzoltar.core.events.IEventListener;
import com.gzoltar.core.events.MultiEventListener;
import com.gzoltar.core.model.Node;
import com.gzoltar.core.model.NodeType;
import com.gzoltar.core.model.Tree;
import com.gzoltar.core.runtime.ProbeGroup.HitProbe;
import com.gzoltar.core.spectrum.SpectrumListener;

public class Collector {

  private static Collector collector;

  private final MultiEventListener listener;

  private final Tree tree;

  private final HitVector hitVector;

  private final SpectrumListener builder;

  public static Collector instance() {
    return collector;
  }

  public static void start(final IEventListener listener) {
    if (collector == null) {
      collector = new Collector(listener);
    }
  }

  public static void restart() {
    if (collector != null) {
      collector = new Collector(collector.listener);
    }
  }

  private Collector(final IEventListener listener) {
    this.listener = new MultiEventListener();
    this.builder = new SpectrumListener();
    addListener(this.builder);
    addListener(listener);

    this.tree = new Tree();
    this.hitVector = new HitVector();
  }

  public void addListener(final IEventListener listener) {
    if (listener != null) {
      this.listener.add(listener);
    }
  }

  public SpectrumListener getBuilder() {
    return this.builder;
  }

  public synchronized Node createNode(final Node parent, final String name, final NodeType type) {
    Node node = this.tree.addNode(name, type, parent.getId());
    this.listener.addNode(node.getId(), name, type, parent.getId());
    return node;
  }

  public synchronized HitProbe createHitProbe(final String groupName, final int nodeId) {
    HitProbe p = this.hitVector.registerProbe(groupName, nodeId);
    this.listener.addProbe(p.getId(), p.getNodeId());
    return p;
  }

  public synchronized void endTransaction(final String transactionName, final boolean isError) {
    this.listener.endTransaction(transactionName, this.hitVector.get(), isError);
  }

  public synchronized void startTransaction() {
    this.hitVector.reset();
  }

  public synchronized void endSession() {
    this.listener.endSession();
  }

  public synchronized boolean[] getHitVector(final String className) {
    return this.hitVector.get(className);
  }

  public synchronized boolean existsHitVector(final String className) {
    return this.hitVector.existsHitVector(className);
  }

  public Node getRootNode() {
    return this.tree.getRoot();
  }

}
